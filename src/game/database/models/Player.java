package game.database.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "player")
@Table(name = "player")
public class Player {
    private int id;
    private String nickname;
    private String password;
    private int gameCount;
    private int wins;
    private int loses;
    private int experience;

    @Id
    @GenericGenerator(name="hilogen", strategy="increment")
    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getGameCount() {
        return gameCount;
    }
    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }

    public int getWins() {
        return wins;
    }
    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }
    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getExperience() {
        return experience;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "player{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", gameCount=" + gameCount +
                ", wins=" + wins +
                ", loses=" + loses +
                ", experience=" + experience + "}";
    }
}
