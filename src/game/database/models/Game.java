package game.database.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class Game {
    private int id;
    private String playerOne;
    private String playerTwo;
    private String winner;
    private String jsonTurns, jsonFieldOne, jsonFieldTwo;
    private int gameLength;

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

    public String getJsonFieldTwo() {
        return jsonFieldTwo;
    }
    public void setJsonFieldTwo(String jsonFieldTwo) {
        this.jsonFieldTwo = jsonFieldTwo;
    }

    public String getJsonFieldOne() {
        return jsonFieldOne;
    }
    public void setJsonFieldOne(String jsonFieldOne) {
        this.jsonFieldOne = jsonFieldOne;
    }

    public int getGameLength() {
        return gameLength;
    }
    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }
}
