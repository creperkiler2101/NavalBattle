package coordinator;

import game.database.Database;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session s = Database.getSession();
        s.close();

        Server server = new Server(25566);
    }
}
