# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```



Here is the link to my Project 2 diagram:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAA5M9qBACu2AMQALADMbgBMAJwgMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMTGUoVHm0-n1YwwMUSjI2AoGTA6FHAADWkoGME2SDA8QVA05MGACFNHHlKAAHmiNOq+eo7gjySp6lKoDyySIVIHKjCnnUYApXShgO72ub0ABRT0qbAEAqYONwmNVajPGDjObqYCMhbLTNQbwJxVzZ3J1Py+Rm9BQyO3VWVYrmeqBJzBYaV-k1+b1MYNpv1FuGF1uzsZtC99AcTB+ukCgelqhImBoHwIBB9in3XeakCm9Fh9kDHnc7QagP3Yy1BQcDiW6XaJoOgDje9JComv4wPq2CGmIoGqAOiIhjAVAQEg25IYY15vv6qi1HeKbogoPh2hiwAkfEL47jhe6Cl+P5-sRdoRiqao0ZqzoUe0EDdoU8GIcGKD1Ex8Tcbxl7RgeRbltiaJ4moZ5YNJ8JBmWCavI6SpLLOwLLORdpiT2MA6b2NyUCWQ4YPUYROE4E4TJpnzGcsuljPpok8UZJlXFupheL4ATQOwjIxCKcCZtIcAKDAAAyEBZIUlnMKpNT1M0bRdL0BjqPkaD2UuXwgr8-yAvsVxmfCsaPHCLwOVMWmQkV+h-DsXzQtV5kpceCDxeKGI9QlBJEmApKYYK8EMkyj5Lq+NK4XRwo6uKf5hjKcoOvVcxASB7Fgdqup-tBsHGqaFphtatr2kuRgQGoJ4QMwXpoi41FzbRB6YfUp7nhJWH3MpobTBR0BIAAXigHDZrm+aFMpJYVbVVaqNOdZzo20CLkDdog+DuzldUnUVElI5OAAjPZSMo7O84YwqWPxDjEObpwr28u9FSff+4aAWNXXITUSAAGaWE0zUlSdKZnVaxU7Ddd1oA9MDHGAIDxC9vP-R1CbMvTjOQzmKB5rlhZa-DBPqWMlO1tT6PNrrUBg0z+NqTGxMwKO5OjJbU7W-WtuY+5evMxhgnjbteEwARcgoCJZEUVR8ELd+kEiSxwFsW9HEiYZfHhwJUZCYmXGeYUGtVbC5ZxYNCkIEppsfebtUFc5+wgu5OfaS5pnmxZyDDjANl2V7zcmW3xe8Z3ZVmCznjeH4-heCg6AxHEiSL8vVe+FgSWCgjjTSJmMWZu0mbdD02WqLl+WOfMLeue3JeT5cfaUBUAMVnV7xaXf3wPxPP-tQrlADmgl6gDS3v1eKW8hpqBGr9DObMOKMjALHP+6BZqIPUBUL8S0JSp20LKeUaDCisXKBNcCB0oJqBgmgI0JpJacWYgQ7I91mDYBRAYUg4oZDaD0AYGAGI7SGGwJQVQBRrT6FYTAAajI-zigwe+fcfNC7fQvGXB4QDAaBwdrjKGhsYYmyAWbNSiMfYzj9guOm2jHZ4xfipcobsPYUzMajGmdtrG42DqzRRDcC7CXjjzUOCDFFahQNwIiFE44GRLgo3C2DFrSDCUyQw+D5DbWCbheoqF0LePmr4o8-M0IhwLiWd+2Ttxw3yeWSc1ZfZjE3iRcgPhcKCzPN3F2B43aDwnD5Ge-l54oj-P4bA4oLQxTRDAAA4kqDQO8UrlgaJM4+Z97BKmvptW+o89Ljy8l3Z2-Zyjvw0hsp+v8dkbgAYY+MIC-HSLRNMqsUSPK8VGoJDJe5JooMidnGJuS9zxNFMtIuTD5CEMYc89A6SyHhy1DoXBh1qHHXoeacFMoWEKzYRw8wMBuE8j4YYQR8RhGiPEZsSRGLpHxVkTitAsT2aVE5qo36pStZaOBjoiGeijYFkqfMi2VtzFo0sT4e2Nj2n9iJn3Ky7sybONqYKtxAd2Vit6dufiNyCmF1ScAeB0LM5gWQDkB5agMR0v5AC+FbYEAwFWVWICJZyHGpgCiEAOxfR5w1ceY1qhxk5GZQOd+3rfV1yMfnVKFZli2rUHWBo4wo0AElpB1lJmEYIgQQSbHiLaFAYZOR7AOMMGARai1jGSKAM0uaGpjG+FGgAclWqEMBOj7NfpKkoYA0qD06BOSNMyY1xqVIm5Nqb03LEzdmytnxFgFrQMWktZaQAVpvvmkEdaG0XCbaqvyc8AgcAAOwRCcCgJwMRMzBDgOFAAbPAe8hgnVFClclBljdGitA6CstZw9l0-zHtE-+3l7Jrqct5Oxb9WUfxHl3X9EKLkAa9kBzZeyrlwk9SGKO6JjUYh+bxNYcBb3GpgSSXV5DkGx2w+gv55ryhfkodqsFxCoXkP2kCo6tCxASxRe5NFAoKXsLUNi3FvD9AEqETAERUAxGzrJTxxWMiIY0rNR+ZRX0zxqKCZrTRkpRW6INty2G9c+WmPla4-2VjlWeJbfYxxsqvYCpM8K7TTtp5qo9eUTm2riMwsjvhpUGJjX1rmIphC1HFoMSmUqNODqvOTPQJQaOf5jWJso1g1DhdEvSHgWBzT6XkOE2fSYiNYwE1JvqCmtNMBLOu0fZ22y3bRi9rmEO0rI6KvOe3QFfwlgwk9U2CvJACQwBdfPBAXrAApNCs6nX+AXWaB97bd4vuaMyTKPQo3rK-sBqD2y-27LKvZbACBgBdagHACAPUoCowAEIxQUHAAA0j0AA6iweNJ8DgNZQE1mAZXAiWay-GJu36tluXOaci4+3DvHdO+dq7N37tPZe29tYH2vs-cAdctzoCYAACsJuYeISscb4oCMoEJLA15Bd3lIKZGR85QWAW0YCaC9aDHSFMbhZQ1jRoOMWi48wmTmL+NcNnXi4TAjRPickxIgXlL7DUvkclpR+XNUqZ+uo9+OsPGct0wY3lyvqne2MzbBzWvbEVSq+2kmnsanIzqYqsz2MOW2N8uqzHtyPOYSp2BZBmH0v05C4CiUTr1o5bZ155jepEVsZ5+F1s6LFZ8c4cwQT8h8Xi6JbHz70g5Yy6ekRxO+TjxMvV+By7qZCNgC5brgz+v+UuON7TPQf4URk5yOKvL1mh426phYxvqZnWk+Gl413nNjURk927lXVDW-og4ND6ACgqDgj8xF7QpJkUWgr-J+fsAkyrhgOmcSnv-s1UTO2NM64q-G2UlU9Syw7MN4TCuFM7oYBdiMpVgcbsGijmCHVkY9+9eveT+5+a4-8W6E+5CfgWgGGvm6WawY+a+iuScko2AMBd6SoF0domeiab+EAsoGBcwacO0+qEcaAKAvWiBaSI+WOVBOqx+hy4GdBuW9ih4BuB2R2lAO+ewz2r2mYPQ12t2d2Nag6JW32LWn+ba-cDQXaEOnBJ2Z20APBiO-Bgh92IhjWYhP2rWvks8HWXgR2fWA2Bh8oiAKYsAwA2AB2hAeQ4ic22Khm+8h8x8p8vQ2oPSdigoRy6OxYymkc3AeApqnmpB9QIAARUAdBQR-EAeMgSS6IVqmexBXuEc5SiuYaSIqRwRmCEcYR5h2qQRpg0RX4iS4Sy454wKlEgEYeIRKERShRrmn0qRkBXmuReAq02gBRaRAeJRySCR7RaS1R2RWSdRNBfiTRbyJ+zwqRN+GqzwIwf2UhVkMhtWMAHhvkQAA
