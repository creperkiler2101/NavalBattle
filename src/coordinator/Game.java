package coordinator;

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

    public Game(User _1, User _2) {
        playerOne = _1;
        playerTwo = _2;

        fieldOne = new int[10][10];
        fieldTwo = new int[10][10];
    }

    public void setShip(int x, int y, int id, int rot, User user) {
        if (user.nickname.equals(playerOne.nickname)) {
            if (rot == 0) {
                for (int i = 0; i < id; i++) {
                    fieldOne[x + i][y] = id;
                }
            }
            else {
                for (int i = 0; i < id; i++) {
                    fieldOne[x][y + i] = id;
                }
            }
        }
        else if (user.nickname.equals(playerTwo.nickname)) {
            if (rot == 0) {
                for (int i = 0; i < id; i++) {
                    fieldTwo[x + i][y] = id;
                }
            }
            else {
                for (int i = 0; i < id; i++) {
                    fieldTwo[x][y + i] = id;
                }
            }
        }
    }
}
