package game.objects.controllers;

import engine.base.Component;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Resources;
import engine.core.Time;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Button;
import engine.ui.Label;
import game.connection.Client;
import game.objects.Game;
import game.objects.ui.MyButton;
import game.scenes.GameScene;
import game.scenes.LoginScene;

import java.awt.*;

public class MainController extends Component {
    private Label mainPanel, profilePanel, profileFrame, profileImage, expBar, expBarFill, searchTimeLabel;
    private MyButton searchButton, exitButton, exitToWindowsButton;

    private Label readyPanel;
    private MyButton acceptButton, declineButton;

    public float searchTime;
    public boolean isInSearch;

    @Override
    protected void start() {
        Client.current.gameFounded = this::gameReady;
        Client.current.gameStart = this::gameStart;
        Client.current.gameNotStarted = this::gameNotStarted;

        mainPanel = new Label();
        mainPanel.sprite = Resources.getSprite("panel1");
        mainPanel.alignType = Align.LEFT;
        mainPanel.getTransform().setScale(new Vector3(1.5f, 1.5f));
        mainPanel.setTextOffset(new Vector3(200, mainPanel.sprite.getImageHeight() * 1.5f - 95));
        mainPanel.left = 50;
        mainPanel.bottom = 100;

        exitButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1 && isActive) {
                    exit();
                }
            }
        };
        exitButton.alignType = Align.LEFT;
        exitButton.bottom -= 150;
        exitButton.left = 220;
        exitButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
        exitButton.setText("exit");
        exitButton.font = FontLoader.getFont("default");
        exitButton.setTextOffset(new Vector3(90, 22));
        exitButton.fontScale = new Vector3(0.8f, 0.8f);

        searchButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1 && isActive) {
                    if (isInSearch)
                        endSearch();
                    else
                        startSearch();
                }
            }
        };
        searchButton.alignType = Align.LEFT;
        searchButton.bottom = 250;
        searchButton.left = 140;
        searchButton.getTransform().setScale(new Vector3(2.4f, 2.4f));
        searchButton.setText("toggle search");
        searchButton.font = FontLoader.getFont("default");
        searchButton.setTextOffset(new Vector3(58, 54));
        searchButton.fontScale = new Vector3(0.6f, 0.6f);

        exitToWindowsButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1 && isActive) {
                    exitToWindows();
                }
            }
        };
        exitToWindowsButton.alignType = Align.LEFT;
        exitToWindowsButton.bottom -= 415;
        exitToWindowsButton.left = 120;
        exitToWindowsButton.getTransform().setScale(new Vector3(2.5f, 2.5f));
        exitToWindowsButton.setText("exit to windows");
        exitToWindowsButton.font = FontLoader.getFont("default");
        exitToWindowsButton.setTextOffset(new Vector3(65, 60));
        exitToWindowsButton.fontScale = new Vector3(0.6f, 0.6f);

        searchTimeLabel = new Label();
        searchTimeLabel.left = -100;
        searchTimeLabel.bottom = 242;
        searchTimeLabel.color = new Color(255, 255 ,255, 0);
        searchTimeLabel.fontColor = new Color(0, 0 ,0, 0);
        searchTimeLabel.fontScale = new Vector3(0.8f, 0.8f);
        searchTimeLabel.sprite = Resources.getSprite("loginPanel");
        searchTimeLabel.alignType = Align.CENTER;
        searchTimeLabel.setTextOffset(new Vector3(100, 100));
        searchTimeLabel.font = FontLoader.getFont("default");

        acceptButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1 && isActive) {
                    accept();
                }
            }
        };
        acceptButton.alignType = Align.CENTER;
        acceptButton.bottom -= 100;
        acceptButton.left = 200;
        acceptButton.getTransform().setScale(new Vector3(1.5f,1.5f));
        acceptButton.setText("accept");
        acceptButton.font = FontLoader.getFont("default");
        acceptButton.setTextOffset(new Vector3(30, 20));
        acceptButton.fontScale = new Vector3(0.6f, 0.6f);

        declineButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1 && isActive) {
                    decline();
                }
            }
        };
        declineButton.alignType = Align.CENTER;
        declineButton.bottom -= 100;
        declineButton.left = -200;
        declineButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
        declineButton.setText("decline");
        declineButton.font = FontLoader.getFont("default");
        declineButton.setTextOffset(new Vector3(30, 20));
        declineButton.fontScale = new Vector3(0.6f, 0.6f);

        readyPanel = new Label();
        readyPanel.sprite = Resources.getSprite("loginPanel");
        readyPanel.alignType = Align.CENTER;
        readyPanel.getTransform().setScale(new Vector3(1f, 1f));
        readyPanel.font = FontLoader.getFont("default");
        readyPanel.setText("game ready!");
        readyPanel.getTransform().setScale(new Vector3(1.5f, 1.5f));
        hideReady();

        addGUI(mainPanel);
        addGUI(exitButton);
        addGUI(exitToWindowsButton);
        addGUI(searchButton);
        addGUI(searchTimeLabel);
        addGUI(readyPanel);
        addGUI(acceptButton);
        addGUI(declineButton);

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));
    }

    @Override
    protected void update() {
        System.out.println(Time.getFps());
        if (isInSearch) {
            searchTime += 1 * Time.getDeltaTime();
            searchTimeLabel.setText(toTimeString((int) Math.floor(searchTime)));
        }
    }

    private void showReady() {
        readyPanel.fontColor = new Color(0, 0, 0, 255);
        readyPanel.color = new Color(255,255,255, 255);
        acceptButton.fontColor = new Color(0, 0, 0, 255);
        acceptButton.color = new Color(255, 255, 255, 255);
        declineButton.fontColor = new Color(0, 0, 0, 255);
        declineButton.color = new Color(255, 255, 255, 255);
        acceptButton.isActive = true;
        declineButton.isActive = true;
    }

    private void hideReady() {
        readyPanel.fontColor = new Color(0, 0, 0, 0);
        readyPanel.color = new Color(255, 255, 255, 0);
        acceptButton.fontColor = new Color(0, 0, 0, 0);
        acceptButton.color = new Color(255, 255, 255, 0);
        declineButton.fontColor = new Color(0, 0, 0, 0);
        declineButton.color = new Color(255, 255, 255, 0);
        acceptButton.isActive = false;
        declineButton.isActive = false;
    }

    private void setActive() {
        exitToWindowsButton.isActive = true;
        exitButton.isActive = true;
        searchButton.isActive = true;
    }

    private void setNotActive() {
        exitToWindowsButton.isActive = false;
        exitButton.isActive = false;
        searchButton.isActive = false;
    }

    public void gameReady() {
        setNotActive();
        showReady();
        endSearch();
    }

    public void accept() {
        send("accept;" + Game.current.player + ";" + Game.current.opponent);
        hideReady();
    }

    public void decline() {
        send("decline;" + Game.current.player + ";" + Game.current.opponent);
        hideReady();
    }

    public void gameNotStarted() {
        setActive();
        hideReady();
    }

    public void gameStart() {
        Application.getCurrent().setScene(GameScene.class);
    }

    private String toTimeString(int seconds) {
        String minutes_str = "";
        String seconds_str = "";

        int minutes = (int)Math.floor(seconds / 60f);
        if (minutes < 10)
            minutes_str += "0";
        minutes_str += minutes;

        seconds -= minutes * 60;
        if (seconds < 10)
            seconds_str += "0";
        seconds_str += seconds;

        return minutes_str + ":" + seconds_str;
    }

    public void exit() {
        Client.current.sendMessage("disconnect;" + Client.current.loggedAs);
        Client.current.isConnected = false;
        Application.getCurrent().setScene(LoginScene.class);
    }

    public void exitToWindows() {
        Client.current.sendMessage("disconnect;" + Client.current.loggedAs);
        Client.current.isConnected = false;
        System.exit(0);
    }

    public void startSearch() {
        searchTime = 0;
        isInSearch = true;
        searchTimeLabel.color = new Color(255, 255 ,255, 255);
        searchTimeLabel.fontColor = new Color(0, 0 ,0, 255);
        //Send to server what we are start search
        send("search;" + Client.current.loggedAs);
    }

    public void endSearch() {
        isInSearch = false;
        searchTimeLabel.color = new Color(255, 255 ,255, 0);
        searchTimeLabel.fontColor = new Color(0, 0 ,0, 0);
        //Send to server what we stop search
        send("stop;" + Client.current.loggedAs);
    }

    private void send(String msg) {
        Client.current.sendMessage(msg);
    }
}
