package game.objects.controllers;

import com.jogamp.opengl.util.texture.Texture;
import coordinator.Turn;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Input;
import engine.core.Resources;
import engine.core.Time;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Label;
import game.connection.Client;
import game.database.Database;
import game.database.models.Rank;
import game.objects.FieldElement;
import game.objects.Game;
import game.objects.ReplayGame;
import game.objects.Ship;
import game.objects.ui.MyButton;
import game.scenes.MainScene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ReplayController extends Component {
    public FieldElement[][] thisField = new FieldElement[10][10];
    public FieldElement[][] opponentField = new FieldElement[10][10];
    public boolean turn = false;
    public boolean isReady = false;
    public boolean isGameEnd = false;

    public String winner = null;

    public static ReplayController current;

    private Texture square = Resources.getSprite("white");

    private MyButton exitButton;
    private Label timeLabel, turnTimeLabel, enemyTurnTimeLabel;
    private Label thisNickLabel, enemyNickLabel;
    private Label winnerLabel;

    private Label myRankFrame, myRankImage;
    private Label enemyRankFrame, enemyRankImage;

    public float thisTimer = 15;
    public float enemyTimer = 15;
    public float time;

    SpriteRenderer sr_1, sr_2;

    @Override
    protected void start() {
        current = this;
        Client.current.setTurn = this::setTurn;
        Client.current.win = this::end;
        Client.current.gameStart = this::gameStart;
        Client.current.updTime = this::resetTimer;

        SpriteRenderer sr_ = getGameObject().addComponent(SpriteRenderer.class);
        sr_.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));

        GameObject seaBG_1 = new GameObject();
        GameObject seaBG_2 = new GameObject();
        instantiate(seaBG_1, new Vector3(100, 1080 - 250 - 640));
        instantiate(seaBG_2, new Vector3(1920 - 100 - 640, 1080 - 250 - 640));

        SpriteRenderer sr_1 = seaBG_1.addComponent(SpriteRenderer.class);
        sr_1.sprite = Resources.getSprite("sea");
        SpriteRenderer sr_2 = seaBG_2.addComponent(SpriteRenderer.class);
        sr_2.sprite = Resources.getSprite("sea");

        GameObject seaBorder_1 = new GameObject();
        GameObject seaBorder_2 = new GameObject();
        GameObject seaBorder_3 = new GameObject();
        instantiate(seaBorder_1, new Vector3(-10000, -10000));
        instantiate(seaBorder_3, new Vector3(100 - 8, 1080 - 250 - 640 - 8, 1));
        instantiate(seaBorder_2, new Vector3(1920 - 100 - 640 - 8, 1080 - 250 - 640 - 8, 1));

        //Не читать тут ничего нет вобще да вам кажется да нет да
        sr_1 = seaBorder_1.addComponent(SpriteRenderer.class);
        sr_1.sprite = Resources.getSprite("fieldBorder");
        //Дальше все есть
        this.sr_1 = seaBorder_2.addComponent(SpriteRenderer.class);
        this.sr_1.sprite = Resources.getSprite("fieldBorder");
        this.sr_2 = seaBorder_3.addComponent(SpriteRenderer.class);
        this.sr_2.sprite = Resources.getSprite("fieldBorder");

        exitButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                if (button == 1 && isActive) {
                    exitGame();
                }
            }
        };
        exitButton.alignType = Align.CENTER;
        exitButton.getTransform().setScale(new Vector3(1.2f, 1.2f));
        exitButton.setText("exit");
        exitButton.font = FontLoader.getFont("default");
        exitButton.setTextOffset(new Vector3(70, 20));
        exitButton.fontScale = new Vector3(0.6f, 0.6f);

        turnTimeLabel = new Label();
        turnTimeLabel.alignType = Align.LEFT_TOP;
        turnTimeLabel.getTransform().setScale(new Vector3(0.3f, 0.3f));
        turnTimeLabel.font = FontLoader.getFont("default");
        turnTimeLabel.sprite = Resources.getSprite("loginPanel");
        turnTimeLabel.setTextOffset(new Vector3(45, 23));
        turnTimeLabel.fontScale = new Vector3(0.4f, 0.4f);
        turnTimeLabel.left = 750;
        turnTimeLabel.top = 150;

        enemyTurnTimeLabel = new Label();
        enemyTurnTimeLabel.alignType = Align.RIGHT_TOP;
        enemyTurnTimeLabel.getTransform().setScale(new Vector3(0.3f, 0.3f));
        enemyTurnTimeLabel.font = FontLoader.getFont("default");
        enemyTurnTimeLabel.sprite = Resources.getSprite("loginPanel");
        enemyTurnTimeLabel.setTextOffset(new Vector3(45, 23));
        enemyTurnTimeLabel.fontScale = new Vector3(0.4f, 0.4f);
        enemyTurnTimeLabel.right = 750;
        enemyTurnTimeLabel.top = 150;

        winnerLabel = new Label();
        winnerLabel.alignType = Align.CENTER;
        winnerLabel.getTransform().setScale(new Vector3(2.5f, 2.5f));
        winnerLabel.font = FontLoader.getFont("default");
        winnerLabel.sprite = Resources.getSprite("loginPanel");
        winnerLabel.setTextOffset(new Vector3(225, 300));
        winnerLabel.fontScale = new Vector3(0.6f, 0.6f);
        winnerLabel.color = new Color(255, 255, 255, 0);

        thisNickLabel = new Label();
        thisNickLabel.alignType = Align.LEFT_TOP;
        thisNickLabel.getTransform().setScale(new Vector3(1.137f, 1.2f));
        thisNickLabel.font = FontLoader.getFont("default");
        thisNickLabel.sprite = Resources.getSprite("EXPpanel");
        thisNickLabel.setTextOffset(new Vector3(145, 115));
        thisNickLabel.fontScale = new Vector3(0.5f, 0.5f);
        thisNickLabel.left = 90;
        thisNickLabel.top = 40;
        thisNickLabel.setText(ReplayGame.current.playerOne);

        enemyNickLabel = new Label();
        enemyNickLabel.alignType = Align.RIGHT_TOP;
        enemyNickLabel.getTransform().setScale(new Vector3(1.137f, 1.2f));
        enemyNickLabel.font = FontLoader.getFont("default");
        enemyNickLabel.sprite = Resources.getSprite("EXPpanel");
        enemyNickLabel.setTextOffset(new Vector3(145, 115));
        enemyNickLabel.fontScale = new Vector3(0.5f, 0.5f);
        enemyNickLabel.right = 90;
        enemyNickLabel.top = 40;
        enemyNickLabel.setText(ReplayGame.current.playerTwo);

        Rank myRank = Database.getRank(Database.getPlayer(ReplayGame.current.playerOne));

        myRankFrame = new Label();
        myRankFrame.sprite = Resources.getSprite("rangFrame");
        myRankFrame.alignType = Align.LEFT_TOP;
        myRankFrame.left = 99;
        myRankFrame.top = 59;
        myRankFrame.font = FontLoader.getFont("default");
        myRankFrame.getTransform().setScale(new Vector3(0.54f, 0.54f, 1f));
        myRankFrame.setText(myRank.getRangname());
        myRankFrame.setTextOffset(new Vector3(136, 60));
        myRankFrame.fontScale = new Vector3(0.3f, 0.3f, 0.3f);

        myRankImage = new Label();
        myRankImage.sprite = Resources.getSprite(myRank.getImage());
        myRankImage.alignType = Align.LEFT_TOP;
        myRankImage.left = 99;
        myRankImage.top = 63;
        myRankImage.getTransform().setScale(new Vector3(0.54f, 0.545f, 1f));

        Rank enemyRank = Database.getRank(Database.getPlayer(ReplayGame.current.playerTwo));

        enemyRankFrame = new Label();
        enemyRankFrame.sprite = Resources.getSprite("rangFrame");
        enemyRankFrame.alignType = Align.RIGHT_TOP;
        enemyRankFrame.right = 90 + 516;
        enemyRankFrame.top = 59;
        enemyRankFrame.font = FontLoader.getFont("default");
        enemyRankFrame.getTransform().setScale(new Vector3(0.54f, 0.54f, 1f));
        enemyRankFrame.setText(enemyRank.getRangname());
        enemyRankFrame.setTextOffset(new Vector3(136, 60));
        enemyRankFrame.fontScale = new Vector3(0.3f, 0.3f, 0.3f);

        enemyRankImage = new Label();
        enemyRankImage.sprite = Resources.getSprite(enemyRank.getImage());
        enemyRankImage.alignType = Align.RIGHT_TOP;
        enemyRankImage.right = 90 + 516;
        enemyRankImage.top = 63;
        enemyRankImage.getTransform().setScale(new Vector3(0.54f, 0.545f, 1f));

        timeLabel = new Label();
        timeLabel.sprite = Resources.getSprite("loginPanel");
        timeLabel.alignType = Align.TOP;
        timeLabel.top = -20;
        timeLabel.font = FontLoader.getFont("default");
        timeLabel.getTransform().setScale(new Vector3(0.8f, 0.8f, 1f));
        timeLabel.setText("00:00");
        timeLabel.setTextOffset(new Vector3(65, 72));
        timeLabel.fontScale = new Vector3(0.8f, 0.8f, 0.8f);

        addGUI(turnTimeLabel);
        addGUI(enemyTurnTimeLabel);
        addGUI(thisNickLabel);
        addGUI(enemyNickLabel);

        addGUI(myRankFrame);
        addGUI(myRankImage);

        addGUI(enemyRankFrame);
        addGUI(enemyRankImage);

        addGUI(timeLabel);

        addGUI(winnerLabel);
        addGUI(exitButton);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Vector3 offset = new Vector3(100 + square.getImageWidth() * x, 1080 - 250 - 64 - square.getImageHeight() * y);

                GameObject gm = new GameObject();
                instantiate(gm, offset);

                FieldElement element = gm.addComponent(FieldElement.class);
                element.x = x;
                element.y = y;
                element.isCurrent = true;

                thisField[x][y] = element;
            }
        }
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Vector3 offset = new Vector3(1920 - 100 - 640 + square.getImageWidth() * x, 1080 - 250 - 64 - square.getImageHeight() * y);

                GameObject gm = new GameObject();
                instantiate(gm, offset);

                FieldElement element = gm.addComponent(FieldElement.class);
                element.x = x;
                element.y = y;
                element.isCurrent = false;

                opponentField[x][y] = element;
            }
        }
    }

    public void gameStart() {
        isReady = true;
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

    private Turn currentTurn;
    private float delay;

    @Override
    protected void update() {
        if (currentTurn == null && !isGameEnd)
            goNext();

        turnTimeLabel.setText((int)Math.floor(thisTimer) + "");
        enemyTurnTimeLabel.setText((int)Math.floor(enemyTimer) + "");

        if (isGameEnd)
            return;

        if (turn) {
            sr_2.color = new Color(150, 255, 150, 255);
            sr_1.color = new Color(255, 255, 255, 255);
            thisTimer -= 1 * Time.getDeltaTime();
        }
        else {
            sr_2.color = new Color(255, 255, 255, 255);
            sr_1.color = new Color(150, 255, 150, 255);
            enemyTimer -= 1 * Time.getDeltaTime();
        }

        time += 1 * Time.getDeltaTime();
        timeLabel.setText(toTimeString((int)Math.floor(time)));

        delay += 1f * Time.getDeltaTime();
        if (delay >= currentTurn.delay) {
            int state = currentTurn.state;
            if (state == 0)
                state = 1;
            else if (state == 3)
                state = 2;

            if (currentTurn.nickname.equals(ReplayGame.current.playerOne)) {
                opponentField[currentTurn.x][currentTurn.y].state = state;
            }
            else {
                thisField[currentTurn.x][currentTurn.y].state = state;
            }

            int count = 0;
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 10; x++) {
                    if (opponentField[x][y].state == 2)
                        count++;
                }
            }

            if (count == 20)
            {
                winner = ReplayGame.current.winner;
                end();
            }

            count = 0;
            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 10; x++) {
                    if (thisField[x][y].state == 2)
                        count++;
                }
            }

            if (count == 20)
            {
                winner = ReplayGame.current.winner;
                end();
            }

            delay = 0;
            currentTurn = null;
        }
    }

    public void goNext() {
        currentTurn = ReplayGame.current.getNext();

        thisTimer = 15;
        enemyTimer = 15;
        turn = ReplayGame.current.playerOne.equals(currentTurn.nickname);
    }

    public void setTurn() {
        turn = true;
        thisTimer = 15;
        enemyTimer = 15;
    }

    public void end() {
        isGameEnd = true;
        exitButton.isActive = true;
        exitButton.color = new Color(255, 255, 255, 255);
        exitButton.fontColor = new Color(0,0, 0, 255);
        winnerLabel.color = new Color(255, 255, 255, 255);
        winnerLabel.setText(winner + " win!");
    }

    public void resetTimer() {
        thisTimer = 15;
        enemyTimer = 15;
    }

    public void exitGame() {
        Game.current = null;
        ReplayController.current = null;
        ReplayGame.current = null;
        Application.getCurrent().setScene(MainScene.class);
    }

    private void send(String msg) {
        Client.current.sendMessage(msg);
    }
}
