package coordinator;

import engine.base.Vector3;
import game.database.Database;
import game.database.models.Player;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Game {
    public User playerOne;
    public User playerTwo;
    public User winner;

    public int[][] fieldOne;
    public int[][] fieldTwo;

    public Ship[] playerOneShips = new Ship[10];
    public Ship[] playerTwoShips = new Ship[10];

    public ArrayList<Turn> turns;
    public int time;

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

    public void saveToDatabase() {
        String json = "";
        JSONArray turnArray = new JSONArray();

        for (int i = 0; i < turns.size(); i++) {
            Turn turn = turns.get(i);

            JSONObject obj = new JSONObject();
            obj.put("nickname", turn.nickname);
            obj.put("x", turn.x);
            obj.put("y", turn.y);
            obj.put("state", turn.state);
            obj.put("delay", turn.delay);

            turnArray.add(obj);
        }

        json = turnArray.toJSONString();

        game.database.models.Game game = new game.database.models.Game();
        game.setGameLength(time);
        game.setPlayerOne(playerOne.nickname);
        game.setPlayerTwo(playerTwo.nickname);
        game.setWinner(winner.nickname);
        game.setJsonTurns(json);

        JSONArray fieldArray = new JSONArray();
        for (int i = 0; i < playerOneShips.length; i++) {
            JSONObject obj = new JSONObject();

            Ship ship = playerOneShips[i];
            obj.put("x", ship.x);
            obj.put("y", ship.y);
            obj.put("size", ship.size);
            obj.put("rotation", ship.rotation);

            fieldArray.add(obj);
        }
        game.setJsonFieldOne(fieldArray.toJSONString());

        fieldArray = new JSONArray();
        for (int i = 0; i < playerTwoShips.length; i++) {
            JSONObject obj = new JSONObject();

            Ship ship = playerTwoShips[i];
            obj.put("x", ship.x);
            obj.put("y", ship.y);
            obj.put("size", ship.size);
            obj.put("rotation", ship.rotation);

            fieldArray.add(obj);
        }
        game.setJsonFieldTwo(fieldArray.toJSONString());

        User loser = null;
        if (winner.nickname.equals(playerOne.nickname))
            loser = playerTwo;
        else
            loser = playerOne;

        Player winnerP = Database.getPlayer(winner.nickname);
        Player loserP = Database.getPlayer(loser.nickname);

        winnerP.setExperience(winnerP.getExperience() + 100);

        int exp = loserP.getExperience();
        exp -= 100;
        if (exp < 0)
            exp = 0;
        loserP.setExperience(exp);

        loserP.setLoses(loserP.getLoses() + 1);
        loserP.setGameCount(loserP.getGameCount() + 1);

        winnerP.setWins(winnerP.getWins() + 1);
        winnerP.setGameCount(winnerP.getGameCount() + 1);

        Database.insert(game);
        Database.update(winnerP);
        Database.update(loserP);
    }

    public Ship getShip(String nickname, int x, int y) {
        for (int i = 0; i < 10; i++) {
            Ship ship = null;
            if (nickname.equals(playerOne.nickname))
                ship = playerOneShips[i];
            else
                ship = playerTwoShips[i];

            for (int offset = 0; offset < ship.size; offset++) {
                if (ship.rotation == 0) {
                    if (x == ship.x + offset && y == ship.y)
                        return ship;
                }
                else if (ship.rotation == 1) {
                    if (x == ship.x && y == ship.y + offset)
                        return ship;
                }
            }
        }

        return null;
    }

    public void loadShips(String nickname, String jsonShips) {
        Ship[] ships = new Ship[10];

        try {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(jsonShips);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject)array.get(i);

                Ship ship = new Ship();
                ship.x = (int)(long)obj.get("x");
                ship.y = (int)(long)obj.get("y");
                ship.rotation = (int)(long)obj.get("rot");
                ship.size = (int)(long)obj.get("size");

                ships[i] = ship;
            }

            if (nickname.equals(playerOne.nickname)) {
                playerOneShips = ships;

                System.out.println("Player one ships: " + jsonShips);
            }
            else {
                playerTwoShips = ships;

                System.out.println("Player two ships: " + jsonShips);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
