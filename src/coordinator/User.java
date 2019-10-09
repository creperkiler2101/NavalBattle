package coordinator;

import java.net.InetAddress;

public class User {
    public InetAddress ip;
    public int port;

    public String nickname = null;

    public boolean isInGame = false;
    public boolean isInSearch = false;

    public User(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
