package game.database.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "game")
@Table(name = "game")
public class Game {
    private int id;
    private String playerOne;
    private String playerTwo;
    private String winner;
    private String jsonTurns;
    private int gameLength;

    @Id
    @GenericGenerator(name="hilogen", strategy="increment")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerOne() {
        return playerOne;
    }
    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }
    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public String getWinner() {
        return winner;
    }
    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getJsonTurns() {
        return jsonTurns;
    }
    public void setJsonTurns(String jsonTurns) {
        this.jsonTurns = jsonTurns;
    }

    public int getGameLength() {
        return gameLength;
    }
    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

    @Override
    public String toString() {
        return
                "game{" +
                        "id=" + id + "," +
                        "playerOne='" + playerOne + "'," +
                        "playerTwo='" + playerTwo + "'," +
                        "winner='" + winner + "'," +
                        "jsonTurns='" + jsonTurns + "'," +
                        "gameLength=" + gameLength + "" +
                        "}"
                ;
    }
}
