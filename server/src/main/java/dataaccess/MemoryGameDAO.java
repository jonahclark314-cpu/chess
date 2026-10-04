package dataaccess;

import model.*;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{

    ArrayList<GameData> listOfGameData;


    public MemoryGameDAO() {
        this.listOfGameData = new ArrayList<>();
    }

    @Override
    public void clear() {
        this.listOfGameData.clear();
    }

}
