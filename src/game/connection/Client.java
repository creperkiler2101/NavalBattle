package game.connection;

import engine.core.Application;
import game.objects.Game;
import game.objects.controllers.GameController;
import game.scenes.MainScene;

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

    public Runnable gameFounded, gameStart, gameNotStarted, setTurn, win, updTime, declined;

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

                //System.out.println(msg);

                if (args[0].equals("connected")) {
                    isConnected = true;
                    onConnected();
                }

                if (args[0].equals("decline")) {
                    isConnected = false;
                    if (declined != null)
                        declined.run();
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

                if (args[0].equals("gameStart")) {
                    if (gameStart != null)
                        gameStart.run();
                }

                if (args[0].equals("notStarted")) {
                    Game.current = null;
                    if (gameNotStarted != null)
                        gameNotStarted.run();
                }

                if (args[0].equals("turn")) {
                    if (setTurn != null)
                        setTurn.run();
                }

                if (args[0].equals("result")) {
                    String nick = args[4];
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int state = Integer.parseInt(args[3]);

                    if (nick.equals(loggedAs)) {
                        GameController.current.opponentField[x][y].state = state;
                    }
                    else {
                        GameController.current.thisField[x][y].state = state;
                    }

                    if (updTime != null)
                        updTime.run();
                }

                if (args[0].equals("playerLeave")) {
                    Game.current = null;
                    Application.getCurrent().setScene(MainScene.class);
                }

                if (args[0].equals("end")) {
                    if (win != null) {
                        GameController.current.winner = args[1];
                        win.run();
                    }
                }

                if (args[0].equals("getTime")) {
                    sendMessage("time;" + loggedAs + ";" + (int)Math.floor(GameController.current.time));
                }

                if (args[0].equals("showShip")) {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int size = Integer.parseInt(args[3]);
                    int rotation = Integer.parseInt(args[4]);

                    GameController.current.showShip(x, y, size, rotation);
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
