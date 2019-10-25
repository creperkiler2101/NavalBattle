package game.objects;

import coordinator.Turn;
import game.database.Database;
import game.objects.controllers.ReplayController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

public class ReplayGame {
    public static ReplayGame current;

    public ArrayList<Turn> turns;
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
        }
        catch (Exception ex) {
            System.out.println("Error loading replay");
            ex.printStackTrace();
        }
    }

    public Turn getNext() {
        turnOffset++;
        return turns.get(turnOffset - 1);
    }
}
