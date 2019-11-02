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
import game.database.Database;
import game.database.models.Player;
import game.database.models.Rank;
import game.objects.Game;
import game.objects.ReplayGame;
import game.objects.ui.MyButton;
import game.scenes.GameScene;
import game.scenes.LoginScene;
import game.scenes.ReplayListScene;
import game.scenes.ReplayScene;

import javax.xml.crypto.Data;
import java.awt.*;

public class MainController extends Component {
    private Label mainPanel, searchTimeLabel;
    private MyButton searchButton, exitButton, exitToWindowsButton, replayButton;

    private Label readyPanel;
    private MyButton acceptButton, declineButton;

    private Label thisNickLabel;
    private Label myRankFrame, myRankImage;
    private Label expFrame, expBar, expBg;
    private Label rKeyL, returnKeyL, LMB;

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


        replayButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1 && isActive) {
                    endSearch();
                    Application.getCurrent().setScene(ReplayListScene.class);
                }
            }
        };
        replayButton.alignType = Align.LEFT;
        replayButton.bottom = 20;
        replayButton.left = 140;
        replayButton.getTransform().setScale(new Vector3(2.4f, 2.4f));
        replayButton.setText("replays");
        replayButton.font = FontLoader.getFont("default");
        replayButton.setTextOffset(new Vector3(125, 54));
        replayButton.fontScale = new Vector3(0.6f, 0.6f);

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
        acceptButton.bottom -= 80;
        acceptButton.left = 175;
        acceptButton.getTransform().setScale(new Vector3(1.5f,1.5f));
        acceptButton.setText("accept");
        acceptButton.font = FontLoader.getFont("default");
        acceptButton.setTextOffset(new Vector3(58, 28));
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
        declineButton.bottom -= 80;
        declineButton.left = -175;
        declineButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
        declineButton.setText("decline");
        declineButton.font = FontLoader.getFont("default");
        declineButton.setTextOffset(new Vector3(55, 28));
        declineButton.fontScale = new Vector3(0.6f, 0.6f);

        readyPanel = new Label();
        readyPanel.sprite = Resources.getSprite("loginPanel");
        readyPanel.alignType = Align.CENTER;
        readyPanel.font = FontLoader.getFont("default");
        readyPanel.setText("game ready!");
        readyPanel.getTransform().setScale(new Vector3(2.5f, 2.5f));
        readyPanel.setTextOffset(new Vector3(240, 350));
        hideReady();

        thisNickLabel = new Label();
        thisNickLabel.alignType = Align.LEFT_TOP;
        thisNickLabel.getTransform().setScale(new Vector3(1.137f, 1.2f));
        thisNickLabel.font = FontLoader.getFont("default");
        thisNickLabel.sprite = Resources.getSprite("EXPpanel");
        thisNickLabel.setTextOffset(new Vector3(145, 115));
        thisNickLabel.fontScale = new Vector3(0.5f, 0.5f);
        thisNickLabel.left = 90 + 1000;
        thisNickLabel.top = 40 + 100;
        thisNickLabel.setText(Client.current.loggedAs);

        Rank myRank = Database.getRank(Database.getPlayer(Client.current.loggedAs));

        myRankFrame = new Label();
        myRankFrame.sprite = Resources.getSprite("rangFrame");
        myRankFrame.alignType = Align.LEFT_TOP;
        myRankFrame.left = 99 + 1000;
        myRankFrame.top = 59 + 100;
        myRankFrame.font = FontLoader.getFont("default");
        myRankFrame.getTransform().setScale(new Vector3(0.54f, 0.54f, 1f));
        myRankFrame.setText(myRank.getRangname());
        myRankFrame.setTextOffset(new Vector3(136, 60));
        myRankFrame.fontScale = new Vector3(0.3f, 0.3f, 0.3f);

        myRankImage = new Label();
        myRankImage.sprite = Resources.getSprite(myRank.getImage());
        myRankImage.alignType = Align.LEFT_TOP;
        myRankImage.left = 99 + 1000;
        myRankImage.top = 63 + 100;
        myRankImage.getTransform().setScale(new Vector3(0.54f, 0.545f, 1f));

        Player p = Database.getPlayer(Client.current.loggedAs);
        Rank next = Database.getNextRank(p);

        expBar = new Label();
        expBar.sprite = Resources.getSprite("expbar");
        expBar.alignType = Align.LEFT_TOP;
        expBar.left = 209 + 1000 + 30;
        expBar.top = 54 + 100 + 110;
        expBar.getTransform().setScale(new Vector3(1f, 1f, 1f));
        expBar.font = FontLoader.getFont("default");
        expBar.setText((p.getExperience() - myRank.getExperience()) + " / " + (next.getExperience() - myRank.getExperience()));
        expBar.fontScale = new Vector3(0.3f, 0.3f, 0.3f);
        expBar.setTextOffset(new Vector3(40, 3));
        expBar.fontColor = new Color(255, 255, 255);

        expBg = new Label();
        expBg.sprite = Resources.getSprite("expbarbg");
        expBg.alignType = Align.LEFT_TOP;
        expBg.left = 200 + 1000 + 30;
        expBg.top = 50 + 100 + 110;
        expBg.getTransform().setScale(new Vector3(1f, 1f, 1f));

        float scale = 1f * (((float)p.getExperience() - myRank.getExperience()) / ((float)next.getExperience() - myRank.getExperience()));
        expBar.getTransform().setScale(new Vector3(scale, 1f, 1f));

        rKeyL = new Label();
        rKeyL.font = FontLoader.getFont("default");
        rKeyL.sprite = Resources.getSprite("RKey");
        rKeyL.setText(" - rotate ship");
        rKeyL.alignType = Align.RIGHT_BOTTOM;
        rKeyL.setTextOffset(new Vector3(60, 16));
        rKeyL.right = 500;
        rKeyL.bottom = 100 + 265;
        rKeyL.fontScale = new Vector3(0.4f, 0.4f);
        rKeyL.getTransform().setScale(new Vector3(1.8f, 1.8f));

        returnKeyL = new Label();
        returnKeyL.font = FontLoader.getFont("default");
        returnKeyL.sprite = Resources.getSprite("ReturnKey");
        returnKeyL.setText(" - toggle fullscreen");
        returnKeyL.alignType = Align.RIGHT_BOTTOM;
        returnKeyL.setTextOffset(new Vector3(60, 16));
        returnKeyL.right = 500;
        returnKeyL.bottom = 0 + 265;
        returnKeyL.fontScale = new Vector3(0.4f, 0.4f);
        returnKeyL.getTransform().setScale(new Vector3(1.8f, 1.8f));

        LMB = new Label();
        LMB.font = FontLoader.getFont("default");
        LMB.sprite = Resources.getSprite("LMB");
        LMB.setText(" - select/set ship");
        LMB.alignType = Align.RIGHT_BOTTOM;
        LMB.setTextOffset(new Vector3(60, 16));
        LMB.right = 500;
        LMB.bottom = -100 + 265;
        LMB.fontScale = new Vector3(0.4f, 0.4f);
        LMB.getTransform().setScale(new Vector3(1.8f, 1.8f));

        Label frame = new Label();
        frame.getTransform().setScale(new Vector3(2f, 3));
        frame.alignType = Align.RIGHT_BOTTOM;
        frame.right = -60;
        frame.bottom = -100;
        frame.sprite = Resources.getSprite("loginPanel");

        addGUI(frame);
        addGUI(rKeyL);
        addGUI(returnKeyL);
        addGUI(LMB);

        addGUI(thisNickLabel);
        addGUI(myRankImage);
        addGUI(myRankFrame);

        addGUI(expBg);
        addGUI(expBar);

        addGUI(mainPanel);
        addGUI(exitButton);
        addGUI(exitToWindowsButton);
        addGUI(searchButton);
        addGUI(replayButton);
        addGUI(searchTimeLabel);
        addGUI(readyPanel);
        addGUI(acceptButton);
        addGUI(declineButton);

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));

        //ReplayGame g = new ReplayGame(Database.getGame(6));
        //Application.getCurrent().setScene(ReplayScene.class);
    }

    @Override
    protected void update() {
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
        replayButton.isActive = true;
    }

    private void setNotActive() {
        exitToWindowsButton.isActive = false;
        exitButton.isActive = false;
        searchButton.isActive = false;
        replayButton.isActive = false;
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
