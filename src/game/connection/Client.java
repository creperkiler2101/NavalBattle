package game.connection;

import game.objects.Game;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    private DatagramSocket client;

    private InetAddress ip;
    private int port;

    public boolean isConnected;
    public String loggedAs;

    private Thread workingThread;

    public static Client current;

    public Runnable gameFounded;

    public Client(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;

        current = this;
    }

    public void connect(String nickname) {
        this.loggedAs = nickname;
        try {
            client = new DatagramSocket();
            workingThread = new Thread() {
                @Override
                public void run() {
                    listen();
                }
            };
            workingThread.start();
        }
        catch (Exception ex) {
            System.out.println("Cannot connect");
            ex.printStackTrace();
        }
    }

    public void listen() {
        sendMessage("connect;" + loggedAs);
        while(true) {
            try {
                byte[] data = new byte[1024];

                DatagramPacket packet = new DatagramPacket(data, data.length);
                client.receive(packet);

                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
                String[] args = msg.split(";");

                System.out.println(msg);

                if (args[0].equals("connected")) {
                    isConnected = true;
                    onConnected();
                }

                if (args[0].equals("decline")) {
                    isConnected = false;
                    onNotConnected();
                }

                if (args[0].equals("founded")) {
                    onGameFounded(args[1]);
                    Game g = new Game();
                    g.player = loggedAs;
                    g.opponent = args[1];

                    if (gameFounded != null)
                        gameFounded.run();
                }
            }
            catch (Exception ex) {
                System.out.println("Error on listening >>>");
                ex.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            byte[] sendData = new byte[1024];
            sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
            client.send(sendPacket);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void onNotConnected() { }
    public void onConnected() { }
    public void onGameFounded(String enemy) { }
}
