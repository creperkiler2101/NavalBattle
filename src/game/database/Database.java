package game.database;

import com.fasterxml.classmate.AnnotationConfiguration;
import game.database.models.Game;
import game.database.models.Player;
import game.database.models.Rank;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String requestURL = "https://ljaljakudrjavtsev17.thkit.ee/NavalBattle/Controller.php?Query={QUERY}&Table={TABLE}";

    public static String request(String query, String table) {
        try {
            System.out.println(query);
            URL url = new URL(requestURL.replace("{QUERY}", query).replace("{TABLE}", table).replace(" ", "%20"));
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return content.toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR";
        }
    }

    private static ArrayList<Game> parseJsonToGameList(String jsonString) {
        ArrayList<Game> result = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(jsonString);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                Game game = new Game();

                game.setId(Integer.parseInt((String)obj.get("id")));
                game.setGameLength(Integer.parseInt((String)obj.get("gameLength")));
                game.setWinner((String)obj.get("winner"));
                game.setJsonTurns((String)obj.get("jsonTurns"));
                game.setPlayerOne((String)obj.get("playerOne"));
                game.setPlayerTwo((String)obj.get("playerTwo"));

                result.add(game);
            }
        }
        catch (Exception ignored) { }

        return result;
    }

    private static ArrayList<Player> parseJsonToPlayerList(String jsonString) {
        ArrayList<Player> result = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(jsonString);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                Player player = new Player();

                player.setId(Integer.parseInt((String)obj.get("id")));
                player.setGameCount(Integer.parseInt((String)obj.get("gameCount")));
                player.setWins(Integer.parseInt((String)obj.get("wins")));
                player.setLoses(Integer.parseInt((String)obj.get("loses")));
                player.setExperience(Integer.parseInt((String)obj.get("experience")));
                player.setNickname((String)obj.get("nickname"));
                player.setPassword((String)obj.get("password"));

                result.add(player);
            }
        }
        catch (Exception ex) { ex.printStackTrace();}

        return result;
    }

    private static ArrayList<Rank> parseJsonToRankList(String jsonString) {
        ArrayList<Rank> result = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(jsonString);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                Rank rank = new Rank();

                rank.setId(Integer.parseInt((String)obj.get("id")));
                rank.setExperience(Integer.parseInt((String)obj.get("experience")));
                rank.setImage((String)obj.get("image"));
                rank.setRangname((String)obj.get("rangname"));

                result.add(rank);
            }
        }
        catch (Exception ignored) { }

        return result;
    }

    public static ArrayList<Game> getGames() {
        String result = request("", "game");
        ArrayList<Game> games = parseJsonToGameList(result);
        return games;
    }

    public static ArrayList<Player> getPlayers() {
        String result = request("", "player");
        ArrayList<Player> players = parseJsonToPlayerList(result);
        return players;
    }

    public static ArrayList<Rank> getRanks() {
        String result = request("", "rang");
        ArrayList<Rank> ranks = parseJsonToRankList(result);
        return ranks;
    }

    public static ArrayList<Game> getGames(String nickname) {
        ArrayList<Game> games = new ArrayList<>();
        List<Game> list = getGames();

        for (int i = 0; i < list.size(); i++) {
            Game p_ = list.get(i);
            if (p_.getPlayerOne().equals(nickname) || p_.getPlayerTwo().equals(nickname)) {
                games.add(p_);
            }
        }

        return games;
    }

    public static Player getPlayer(String nickname) {
        Player p = null;
        ArrayList<Player> list = getPlayers();

        for (int i = 0; i < list.size(); i++) {
            Player p_ = list.get(i);
            if (p_.getNickname().equals(nickname)) {
                p = p_;
                break;
            }
        }

        return p;
    }

    public static boolean isPlayerExists(String nickname) {
        Player p = null;

        ArrayList<Player> list = getPlayers();
        for (int i = 0; i < list.size(); i++) {
            Player p_ = list.get(i);
            if (p_.getNickname().equals(nickname)) {
                p = p_;
                break;
            }
        }

        return p != null;
    }

    public static Rank getRank(Player p) {
        Rank r = null;

        ArrayList<Rank> list = getRanks();

        r = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            Rank rank = list.get(i);
            if (p.getExperience() >= rank.getExperience())
                r = rank;
        }

        return r;
    }

    public static Rank getNextRank(Player p) {
        Rank r = null;

        ArrayList<Rank> list = getRanks();

        r = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            Rank rank = list.get(i);
            if (p.getExperience() >= rank.getExperience()) {
                if (i != list.size() - 1)
                    r = list.get(i + 1);
                else
                    r = rank;
            }
        }

        return r;
    }

    public static boolean logIn(String nickname, String password) {
        Player p = null;
        ArrayList<Player> list = getPlayers();

        for (int i = 0; i < list.size(); i++) {
            Player p_ = list.get(i);
            if (p_.getNickname().equals(nickname)) {
                if (p_.getPassword().equals(password)) {
                    p = p_;
                    break;
                }
            }
        }

        return p != null;
    }

    public static void insert(Game game) {
        String sql = "INSERT INTO game(playerOne, playerTwo, jsonTurns, winner, gameLength) values ('{PO}', '{PT}', '{JT}', {W}, {GL})";
        sql = sql.replace("{PO}", game.getPlayerOne()).replace("{PT}", game.getPlayerTwo())
                .replace("{JT}", game.getJsonTurns()).replace("{W}", game.getWinner())
                .replace("{GL}", Integer.toString(game.getGameLength()));
        request(sql, "game");
    }

    public static void update(Game game) {
        String sql = "UPDATE game SET playerOne='{PO}', playerTwo='{PT}', jsonTurns='{JT}', winner='{W}', gameLength={GL} WHERE id={ID}";
        sql = sql.replace("{PO}", game.getPlayerOne()).replace("{PT}", game.getPlayerTwo())
                .replace("{JT}", game.getJsonTurns()).replace("{W}", game.getWinner())
                .replace("{GL}", Integer.toString(game.getGameLength())).replace("{ID}", Integer.toString(game.getId()));
        request(sql, "game");
    }

    public static void insert(Player player) {
        String sql = "INSERT INTO player(nickname, password, gameCount, wins, loses, experience) values ('{NI}', '{PA}', {GC}, {W}, {L}, {E})";
        sql = sql.replace("{NI}", player.getNickname()).replace("{PA}", player.getPassword())
                .replace("{GC}", Integer.toString(player.getGameCount())).replace("{W}", Integer.toString(player.getWins()))
                .replace("{L}", Integer.toString(player.getLoses())).replace("{E}", Integer.toString(player.getExperience()));
        String result = request(sql, "player");
        System.out.println(result);
    }

    public static void update(Player player) {
        String sql = "UPDATE player SET nickname='{NI}', password='{PA}', gameCount={GC}, wins={W}, loses={L}, experience={E} WHERE id = {ID}";
        sql = sql.replace("{NI}", player.getNickname()).replace("{PA}", player.getPassword())
                .replace("{GC}", Integer.toString(player.getGameCount())).replace("{W}", Integer.toString(player.getWins()))
                .replace("{L}", Integer.toString(player.getLoses())).replace("{E}", Integer.toString(player.getExperience()))
                .replace("{ID}", Integer.toString(player.getId()));
        request(sql, "player");
    }

    public static void insert(Rank rank) {
        String sql = "INSERT INTO rang(rangname, experience, image) VALUES('{RN}',{E},'{I}')";
        sql = sql.replace("{RN}", rank.getRangname()).replace("{E}", Integer.toString(rank.getExperience()))
                .replace("{I}", rank.getImage());
        request(sql, "rang");
    }

    public static void update(Rank rank) {
        String sql = "UPDATE rang SET rangname='{RN}', experience={E}, image='{I}' WHERE id={ID}";
        sql = sql.replace("{RN}", rank.getRangname()).replace("{E}", Integer.toString(rank.getExperience()))
                .replace("{I}", rank.getImage()).replace("{ID}", Integer.toString(rank.getId()));
        request(sql, "rang");
    }
}
