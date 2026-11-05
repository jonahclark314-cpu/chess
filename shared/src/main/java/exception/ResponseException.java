package exception;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public enum Code {
        ServerError,
        ClientError,
        BadRequest,
        Unauthorized,
        AlreadyTaken,
    }

    final private Code code;

    public ResponseException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage(), "status", code));
    }

    public static ResponseException fromJson(String json, int httpResponse) {
        var map = new Gson().fromJson(json, HashMap.class);
        Object mesageValue = map.get("message");
        String message;
        if (mesageValue == null) {
            message = "There was no message.";
        } else {
            message = mesageValue.toString();
        }
        Code code = fromHttpStatusCode(httpResponse);
        return new ResponseException(code, message);
    }

    public Code code() {
        return code;
    }

    public static Code fromHttpStatusCode(int httpStatusCode) {
        return switch (httpStatusCode) {
            case 500 -> Code.ServerError;
            case 401 -> Code.Unauthorized;
            case 403 -> Code.AlreadyTaken;
            case 400 -> Code.BadRequest;
            default -> throw new IllegalArgumentException("Unknown HTTP status code: " + httpStatusCode);
        };
    }

    public int toHttpStatusCode() {
        return switch (code) {
            case ServerError -> 500;
            case BadRequest, ClientError -> 400;
            case AlreadyTaken -> 403;
            case Unauthorized -> 401;
        };
    }
}