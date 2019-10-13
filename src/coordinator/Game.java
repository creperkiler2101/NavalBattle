package coordinator;

import engine.base.Vector3;

import java.util.ArrayList;

public class Game {
    public final int smallShip = 1;
    public final int mediumShip = 2;
    public final int largeShip = 3;
    public final int largestShip = 4;

    public User playerOne;
    public User playerTwo;

    public int[][] fieldOne;
    public int[][] fieldTwo;

    public ArrayList<Vector3> turns;

    public Game(User _1, User _2) {
        turns = new ArrayList<>();
        playerOne = _1;
        playerTwo = _2;

        fieldOne = new int[10][10];
        fieldTwo = new int[10][10];

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                fieldOne[x][y] = 1;
                fieldTwo[x][y] = 1;
            }
        }
    }

    public boolean isAllDestroyed(String nick) {
        int[][] field = null;
        if (nick.equals(playerOne.nickname))
            field = fieldOne;
        else
            field = fieldTwo;

        int count = 0;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (field[x][y] == 6)
                    count++;
            }
        }

        return count == 20;
    }
}
