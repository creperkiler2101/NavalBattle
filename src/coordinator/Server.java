package coordinator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    private DatagramSocket server;

    public ArrayList<User> connections;

    private int port;
    private InetAddress ip;

    public Server(int port) {
        this.connections = new ArrayList<>();
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
        return null;
    }

    private void gameNotStarted(Game game) {

    }

    private void gameEnd(Game game) {

    }

    private void gameReady(Game game) {

    }

    private void gameSearch() {
        while (true) {
            try {
                if (connections.size() < 2)
                    continue;

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

                if (user_1 != null && user_2 != null) {
                    user_1.isInSearch = false;
                    user_2.isInSearch = false;
                    user_1.isInGame = true;
                    user_2.isInGame = true;

                    sendMessage(user_1, "founded;" + user_2.nickname);
                    sendMessage(user_2, "founded;" + user_1.nickname);
                }
            }
            catch (Exception ignored) { }
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

                User user = getUser(senderIp);

                if (args.length == 0)
                    continue;

                if (args[0].equals("connect")) {
                    if (isInOnline(args[1])) {
                        sendMessage(user, "decline");
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
                    print(user.nickname + " disconnected");
                }

                if (args[0].equals("search")) {
                    user.isInSearch = true;
                    print(user.nickname + " start search");
                }

                if (args[0].equals("stop")) {
                    user.isInSearch = false;
                    print(user.nickname + " stop search");
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

    private boolean isInOnline(String nickname) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).nickname.equals(nickname))
                return true;
        }

        return false;
    }
}
