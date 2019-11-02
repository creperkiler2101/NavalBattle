package coordinator;

import engine.base.Vector3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    private DatagramSocket server;

    public ArrayList<User> connections;
    public ArrayList<Game> games;

    private int port;
    private InetAddress ip;

    public Server(int port) {
        this.connections = new ArrayList<>();
        this.games = new ArrayList<>();
        this.port = port;

        try {
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                this.ip = InetAddress.getByName(socket.getLocalAddress().getHostAddress());
            }
        } catch(Exception ignored) { }

        try {
            System.out.println("Starting server at " + ip.getHostAddress() + ":" + port);
            this.server = new DatagramSocket(port);
            listen();
        }
        catch (Exception ex) {
            System.out.println("Error on starting >>> ");
            ex.printStackTrace();
        }
    }

    private Game getGame(String nick_1, String nick_2) {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game.playerOne.nickname.equals(nick_1) && game.playerTwo.nickname.equals(nick_2))
                return game;
            if (game.playerOne.nickname.equals(nick_2) && game.playerTwo.nickname.equals(nick_1))
                return game;
        }
        return null;
    }

    private Game getGame(String nick_1) {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game.playerOne.nickname.equals(nick_1))
                return game;
            if (game.playerTwo.nickname.equals(nick_1))
                return game;
        }
        return null;
    }

    private void gameNotStarted(Game game) {
        sendMessage(game.playerOne, "notStarted");
        sendMessage(game.playerTwo, "notStarted");
    }

    private void gameEnd(Game game) {

    }

    private void gameReady(Game game) {
        sendMessage(game.playerOne, "ready");
        sendMessage(game.playerTwo, "ready");
    }

    private void gameSearch() {
        while (true) {
            try {
                User user_1 = null;
                User user_2 = null;

                for (int i = 0; i < connections.size(); i++) {
                    User buffer = connections.get(i);
                    if (!buffer.isInSearch)
                        continue;

                    if (user_1 == null) {
                        user_1 = buffer;
                        continue;
                    }

                    if (user_2 == null) {
                        user_2 = buffer;
                        continue;
                    }

                    if (user_1 != null && user_2 != null)
                        break;
                }

                Thread.sleep(10);
                if (user_1 != null && user_2 != null) {
                    user_1.isInSearch = false;
                    user_2.isInSearch = false;
                    user_1.isInGame = true;
                    user_2.isInGame = true;

                    sendMessage(user_1, "founded;" + user_2.nickname);
                    sendMessage(user_2, "founded;" + user_1.nickname);

                    print("game founded " + user_1.nickname + "; " + user_2.nickname);
                    games.add(new Game(user_1, user_2));
                }
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    private void listen() {
        System.out.println("Started");
        Thread searchThread = new Thread() {
            @Override
            public void run() {
                gameSearch();
            }
        };
        searchThread.start();

        while(true) {
            try {
                byte[] data = new byte[1024];

                DatagramPacket packet = new DatagramPacket(data, data.length);
                server.receive(packet);

                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                String[] args = msg.split(";");

                InetAddress senderIp = packet.getAddress();
                int senderPort = packet.getPort();

                System.out.println("recieved message: " + msg + "; from " + senderIp.getHostAddress());

                if (args.length == 0)
                    continue;

                User user = getUser(args[1]);

                if (args[0].equals("connect")) {
                    if (isInOnline(args[1])) {
                        User user_ = new User(senderIp, senderPort);
                        sendMessage(user_, "decline");
                        continue;
                    }
                    user = new User(senderIp, senderPort);
                    user.nickname = args[1];
                    connections.add(user);

                    System.out.println(senderIp.getHostAddress() + "(" + user.nickname + ") connected");
                    sendMessage(user, "connected");
                }

                if (args[0].equals("disconnect")) {
                    connections.remove(user);
                    Game g = getGame(user.nickname);
                    if (g != null) {
                        if (!user.nickname.equals(g.playerOne.nickname))
                            sendMessage(g.playerOne, "playerLeave");
                        else
                            sendMessage(g.playerTwo, "playerLeave");
                        games.remove(g);
                    }
                    print(user.nickname + " disconnected");
                }

                if (args[0].equals("search")) {
                    user.isInSearch = true;
                    user.isReady = false;
                    user.isInGame = false;
                    user.isLoaded = false;
                    print(user.nickname + " start search");
                }

                if (args[0].equals("stop")) {
                    user.isInSearch = false;
                    print(user.nickname + " stop search");
                }

                if (args[0].equals("accept")) {
                    Game game = getGame(user.nickname, args[2]);
                    if (game == null)
                        continue;
                    user.isReady = true;
                    print(user.nickname + " accept the game");

                    if (game.playerOne.isReady && game.playerTwo.isReady) {
                        sendMessage(game.playerOne, "gameStart");
                        sendMessage(game.playerTwo, "gameStart");
                    }
                }

                if (args[0].equals("decline")) {
                    Game game = getGame(user.nickname, args[2]);
                    sendMessage(game.playerOne, "notStarted");
                    sendMessage(game.playerTwo, "notStarted");
                    game.playerOne.isReady = false;
                    game.playerTwo.isReady = false;
                    games.remove(game);

                    print(user.nickname + " decline the game");
                }

                if (args[0].equals("go")) {
                    Game g = getGame(args[1]);
                    user.isLoaded = true;

                    print(args[1] + " loaded");
                    if (g.playerOne.isLoaded && g.playerTwo.isLoaded) {
                        int i = (int)Math.round(Math.random());
                        sendMessage(g.playerOne, "gameStart");
                        sendMessage(g.playerTwo, "gameStart");
                        if (i == 0)
                            sendMessage(g.playerOne, "turn");
                        else
                            sendMessage(g.playerTwo, "turn");
                        print(i + " turn");
                    }
                }

                if (args[0].equals("shot")) {
                    int x = Integer.parseInt(args[2]);
                    int y = Integer.parseInt(args[3]);

                    Game g = getGame(user.nickname);
                    int state = -1;
                    if (user.nickname.equals(g.playerOne.nickname)) {
                        state = g.fieldTwo[x][y];
                        if (state == 3) {
                            g.fieldTwo[x][y] = 6;

                            Ship ship = g.getShip(g.playerTwo.nickname, x, y);
                            if (ship != null && ship.isDestroyed(g.fieldTwo)) {
                                ArrayList<Vector3> nearby = ship.getNearby();

                                for (int i = 0; i < nearby.size(); i++) {
                                    Vector3 cell = nearby.get(i);
                                    int state_ = g.fieldTwo[(int)cell.x][(int)cell.y];
                                    if (state_ == 0) {
                                        sendMessage(g.playerOne, "result;" + (int)cell.x + ";" + (int)cell.y + ";" + 1 + ";" + user.nickname);
                                        sendMessage(g.playerTwo, "result;" + (int)cell.x + ";" + (int)cell.y + ";" + 1 + ";" + user.nickname);
                                    }
                                }

                                sendMessage(g.playerOne, "showShip;" + ship.x + ";" + ship.y + ";" + ship.size + ";" + ship.rotation);
                            }

                            sendMessage(g.playerOne, "turn");
                        }
                        else
                            sendMessage(g.playerTwo, "turn");
                    }
                    else {
                        state = g.fieldOne[x][y];
                        if (state == 3) {
                            g.fieldOne[x][y] = 6;
                            Ship ship = g.getShip(g.playerOne.nickname, x, y);
                            if (ship != null && ship.isDestroyed(g.fieldOne)) {
                                ArrayList<Vector3> nearby = ship.getNearby();

                                for (int i = 0; i < nearby.size(); i++) {
                                    Vector3 cell = nearby.get(i);
                                    int state_ = g.fieldOne[(int)cell.x][(int)cell.y];
                                    if (state_ == 0) {
                                        sendMessage(g.playerOne, "result;" + (int)cell.x + ";" + (int)cell.y + ";" + 1 + ";" + user.nickname);
                                        sendMessage(g.playerTwo, "result;" + (int)cell.x + ";" + (int)cell.y + ";" + 1 + ";" + user.nickname);
                                    }
                                }

                                sendMessage(g.playerTwo, "showShip;" + ship.x + ";" + ship.y + ";" + ship.size + ";" + ship.rotation);
                            }

                            sendMessage(g.playerTwo, "turn");
                        }
                        else
                            sendMessage(g.playerOne, "turn");
                    }

                    Turn turn = new Turn();
                    turn.delay = Float.parseFloat(args[4]);
                    turn.state = state;
                    turn.x = x;
                    turn.y = y;
                    turn.nickname = user.nickname;
                    g.turns.add(turn);

                    if (state == 3)
                        state = 2;
                    else
                        state = 1;

                    if (g.isAllDestroyed(g.playerOne.nickname))
                    {
                        for (int i = 0; i < 10; i++) {
                            Ship ship = g.playerOneShips[i];
                            sendMessage(g.playerTwo, "showShip;" + ship.x + ";" + ship.y + ";" + ship.size + ";" + ship.rotation);

                            ship = g.playerTwoShips[i];
                            sendMessage(g.playerOne, "showShip;" + ship.x + ";" + ship.y + ";" + ship.size + ";" + ship.rotation);
                        }

                        g.winner = g.playerTwo;
                        sendMessage(g.playerOne, "end;" + g.playerTwo.nickname);
                        sendMessage(g.playerTwo, "end;" + g.playerTwo.nickname);
                        sendMessage(g.playerTwo, "getTime");
                    }
                    else if (g.isAllDestroyed(g.playerTwo.nickname))
                    {
                        for (int i = 0; i < 10; i++) {
                            Ship ship = g.playerOneShips[i];
                            sendMessage(g.playerTwo, "showShip;" + ship.x + ";" + ship.y + ";" + ship.size + ";" + ship.rotation);

                            ship = g.playerTwoShips[i];
                            sendMessage(g.playerOne, "showShip;" + ship.x + ";" + ship.y + ";" + ship.size + ";" + ship.rotation);
                        }

                        g.winner = g.playerOne;
                        sendMessage(g.playerOne, "end;" + g.playerOne.nickname);
                        sendMessage(g.playerTwo, "end;" + g.playerOne.nickname);
                        sendMessage(g.playerOne, "getTime");
                    }

                    sendMessage(g.playerOne, "result;" + x + ";" + y + ";" + state + ";" + user.nickname);
                    sendMessage(g.playerTwo, "result;" + x + ";" + y + ";" + state + ";" + user.nickname);
                    print(user.nickname + " shot at " + x + " " + y + " state " + state);
                }

                if (args[0].equals("field")) {
                    Game g = getGame(user.nickname);
                    int offset = 2;
                    int[][] field = new int[10][10];

                    String fieldS = "";
                    for (int y = 0; y < 10; y++) {
                        for (int x = 0; x < 10; x++) {
                            String s = args[offset + x + y * 10];
                            field[x][y] = Integer.parseInt(s);
                            fieldS += s;
                        }
                        fieldS += "\n";
                    }

                    g.loadShips(user.nickname, args[offset + 99 + 1]);

                    if (user.nickname.equals(g.playerOne.nickname)) {
                        g.fieldOne = field;
                    }
                    else if (user.nickname.equals(g.playerTwo.nickname)) {
                        g.fieldTwo = field;
                    }
                }

                if (args[0].equals("next")) {
                    Game g = getGame(user.nickname);
                    Turn turn = new Turn();
                    turn.delay = 15;
                    turn.state = -1;
                    turn.x = -1;
                    turn.y = -1;
                    turn.nickname = user.nickname;
                    g.turns.add(turn);

                    if (user.nickname.equals(g.playerOne.nickname)) {
                        sendMessage(g.playerTwo, "turn");
                    }
                    else if (user.nickname.equals(g.playerTwo.nickname)) {
                        sendMessage(g.playerOne, "turn");
                    }
                }

                if (args[0].equals("time")) {
                    Game g = getGame(args[1]);
                    if (g == null)
                        continue;
                    g.time = Integer.parseInt(args[2]);
                    games.remove(g);

                    g.playerOne.isLoaded = false;
                    g.playerOne.isInGame = false;
                    g.playerOne.isReady = false;

                    g.playerTwo.isLoaded = false;
                    g.playerTwo.isInGame = false;
                    g.playerTwo.isReady = false;

                    g.saveToDatabase();
                }
            }
            catch (Exception ex) {
                System.out.println("Error on listening >>>");
                ex.printStackTrace();
            }
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private void sendToAll(String message) {
        for (int i = 0; i < connections.size(); i++) {
            sendMessage(connections.get(i), message);
        }
    }

    private void sendMessage(User user, String message) {
        try {
            byte[] sendData = new byte[1024];
            sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, user.ip, user.port);
            server.send(sendPacket);
        }
        catch (Exception ignored) { }
    }

    private User getUser(InetAddress ip) {
        User result = null;

        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).ip.getHostAddress().equals(ip.getHostAddress()))
            {
                result = connections.get(i);
                break;
            }
        }

        return result;
    }

    private User getUser(String nick) {
        User result = null;

        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).nickname.equals(nick))
            {
                result = connections.get(i);
                break;
            }
        }

        return result;
    }

    private boolean isInOnline(String nickname) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).nickname.equals(nickname))
                return true;
        }

        return false;
    }
}
