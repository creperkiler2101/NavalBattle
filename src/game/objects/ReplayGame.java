package game.objects;

import coordinator.Turn;
import engine.base.GameObject;
import game.database.Database;
import game.objects.controllers.ReplayController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class ReplayGame {
    public static ReplayGame current;

    public ArrayList<Turn> turns;

    public coordinator.Ship[] playerOneShips = new coordinator.Ship[10];
    public coordinator.Ship[] playerTwoShips = new coordinator.Ship[10];

    public int turnOffset;

    public String playerOne, playerTwo, winner;

    public ReplayGame(game.database.models.Game game) {
        current = this;

        turns = new ArrayList<>();

        playerOne = game.getPlayerOne();
        playerTwo = game.getPlayerTwo();
        winner = game.getWinner();

        String json = game.getJsonTurns();

        try {
            JSONParser p = new JSONParser();
            JSONArray array = (JSONArray) p.parse(json);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject)array.get(i);

                Turn turn = new Turn();
                turn.nickname = (String)obj.get("nickname");
                turn.delay = (float)(double)obj.get("delay");
                turn.state = (int)(long)obj.get("state");
                turn.x = (int)(long)obj.get("x");
                turn.y = (int)(long)obj.get("y");

                turns.add(turn);
            }

            JSONArray pOneShipsArray = (JSONArray) p.parse(game.getJsonFieldOne());
            JSONArray pTwoShipsArray = (JSONArray) p.parse(game.getJsonFieldTwo());

            for (int i = 0; i < 10; i++) {
                JSONObject oneObj = (JSONObject)pOneShipsArray.get(i);
                JSONObject twoObj = (JSONObject)pTwoShipsArray.get(i);

                playerOneShips[i] = loadShip(oneObj);
                playerTwoShips[i] = loadShip(twoObj);
            }
        }
        catch (Exception ex) {
            System.out.println("Error loading replay");
            ex.printStackTrace();
        }
    }

    private coordinator.Ship loadShip(JSONObject obj) {
        coordinator.Ship ship = new coordinator.Ship();
        ship.x = (int)(long)obj.get("x");
        ship.y = (int)(long)obj.get("y");
        ship.size = (int)(long)obj.get("size");
        ship.rotation = (int)(long)obj.get("rotation");

        if (ship.size == 1) {
            ship.sprite = "smallShip";
        }
        else if (ship.size == 2) {
            ship.sprite = "mediumShip";
        }
        else if (ship.size == 3) {
            ship.sprite = "largeShip";
        }
        else if (ship.size == 4) {
            ship.sprite = "largestShip";
        }

        return ship;
    }

    public Turn getNext() {
        turnOffset++;
        return turns.get(turnOffset - 1);
    }
}
