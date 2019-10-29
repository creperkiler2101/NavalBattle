package coordinator;

import engine.base.Vector3;
import game.database.Database;
import game.database.models.Player;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Game {
    public final int smallShip = 1;
    public final int mediumShip = 2;
    public final int largeShip = 3;
    public final int largestShip = 4;

    public User playerOne;
    public User playerTwo;
    public User winner;

    public int[][] fieldOne;
    public int[][] fieldTwo;

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

        try (Session session = Database.getSession()) {
            session.beginTransaction();

            game.database.models.Game game = new game.database.models.Game();
            game.setGameLength(time);
            game.setPlayerOne(playerOne.nickname);
            game.setPlayerTwo(playerTwo.nickname);
            game.setWinner(winner.nickname);
            game.setJsonTurns(json);

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

            session.save(game);
            session.update(loserP);
            session.update(winnerP);

            session.getTransaction().commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
