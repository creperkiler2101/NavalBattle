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
import game.objects.ui.MyButton;
import game.scenes.LoginScene;

import java.awt.*;

public class MainController extends Component {
    private Label mainPanel, profilePanel, profileFrame, profileImage, expBar, expBarFill, searchTimeLabel;
    private Button searchButton, exitButton, exitToWindowsButton;

    public float searchTime;
    public boolean isInSearch;

    @Override
    protected void start() {
        Client.current.gameFounded = this::gameStart;

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
                if (button == 1) {
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
                if (button == 1) {
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
                if (button == 1) {
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
        searchTimeLabel.color = new Color(255, 255 ,255, 0);
        searchTimeLabel.fontColor = new Color(0, 0 ,0, 0);
        searchTimeLabel.fontScale = new Vector3(0.8f, 0.8f);
        searchTimeLabel.sprite = Resources.getSprite("loginPanel");
        searchTimeLabel.alignType = Align.CENTER;
        searchTimeLabel.left = -100;
        searchTimeLabel.bottom = 242;
        searchTimeLabel.setTextOffset(new Vector3(100, 100));
        searchTimeLabel.font = FontLoader.getFont("default");

        addGUI(mainPanel);
        addGUI(exitButton);
        addGUI(exitToWindowsButton);
        addGUI(searchButton);
        addGUI(searchTimeLabel);

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));
    }

    @Override
    protected void update() {
        if (isInSearch) {
            searchTime += 1 * Time.getDeltaTime();
            searchTimeLabel.setText(toTimeString((int) Math.floor(searchTime)));
        }
    }

    public void gameStart() {

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
        Client.current.sendMessage("disconnect");
        Client.current.isConnected = false;
        Application.getCurrent().setScene(LoginScene.class);
    }

    public void exitToWindows() {
        Client.current.sendMessage("disconnect");
        Client.current.isConnected = false;
        System.exit(0);
    }

    public void startSearch() {
        searchTime = 0;
        isInSearch = true;
        searchTimeLabel.color = new Color(255, 255 ,255, 255);
        searchTimeLabel.fontColor = new Color(0, 0 ,0, 255);
        //Send to server what we are start search
        send("search");
    }

    public void endSearch() {
        isInSearch = false;
        searchTimeLabel.color = new Color(255, 255 ,255, 0);
        searchTimeLabel.fontColor = new Color(0, 0 ,0, 0);
        //Send to server what we stop search
        send("stop");
    }

    private void send(String msg) {
        Client.current.sendMessage(msg);
    }
}
