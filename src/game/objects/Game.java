package game.objects;

public class Game {
    public static Game current;
    public String player;
    public String opponent;

    public Game() {
        current = this;
    }
}
