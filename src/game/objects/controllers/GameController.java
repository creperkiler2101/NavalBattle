package game.objects.controllers;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
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
import game.objects.Ship;
import game.objects.ui.MyButton;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GameController extends Component {
    public FieldElement[][] thisField = new FieldElement[10][10];
    public FieldElement[][] opponentField = new FieldElement[10][10];
    public boolean turn = false;
    public boolean isReady = false;
    public boolean isPressReady = false;
    public boolean isGameEnd = false;

    public String winner = null;

    public static GameController current;

    private Texture square = Resources.getSprite("white");

    private MyButton goButton;
    private Label timeLabel, turnTimeLabel, enemyTurnTimeLabel;
    private Label thisNickLabel, enemyNickLabel;
    private Label winnerLabel;

    //248 248
    private Label myRankFrame, myRankImage;

    private float thisTimer = 15;
    private float enemyTimer = 15;
    private float time;

    public Ship[] ships = new Ship[10];
    public Ship selectedShip;

    SpriteRenderer sr_1, sr_2;

    public boolean isAllShipsReady = false;

    @Override
    protected void start() {
        current = this;
        Client.current.setTurn = this::setTurn;
        Client.current.win = this::end;
        Client.current.gameStart = this::gameStart;

        GameObject seaBG_1 = new GameObject();
        GameObject seaBG_2 = new GameObject();
        instantiate(seaBG_1, new Vector3(100, 1080 - 250 - 640));
        instantiate(seaBG_2, new Vector3(1920 - 100 - 640, 1080 - 250 - 640));

        for (int i = 0; i < 4; i++) {
            GameObject ship = new GameObject();
            instantiate(ship, new Vector3(10 + 74 * i, 10));
            SpriteRenderer sr = ship.addComponent(SpriteRenderer.class);
            sr.sprite = Resources.getSprite("smallShip");
            Ship comp = ship.addComponent(Ship.class);
            comp.size = 1;
            comp.startPos =  new Vector3(10 + 74 * i, 10);
            ships[i] = comp;
        }

        for (int i = 0; i < 3; i++) {
            GameObject ship = new GameObject();
            instantiate(ship, new Vector3(10 + 138 * i, 20 + 64));
            SpriteRenderer sr = ship.addComponent(SpriteRenderer.class);
            sr.sprite = Resources.getSprite("mediumShip");
            Ship comp = ship.addComponent(Ship.class);
            comp.size = 2;
            comp.startPos = new Vector3(10 + 138 * i, 20 + 64);
            ships[i + 4] = comp;
        }

        for (int i = 0; i < 2; i++) {
            GameObject ship = new GameObject();
            instantiate(ship, new Vector3(400 + 202 * i, 20 + 64));
            SpriteRenderer sr = ship.addComponent(SpriteRenderer.class);
            sr.sprite = Resources.getSprite("largeShip");
            Ship comp = ship.addComponent(Ship.class);
            comp.size = 3;
            comp.startPos = new Vector3(400 + 202 * i, 20 + 64);
            ships[i + 7] = comp;
        }

        GameObject ship = new GameObject();
        instantiate(ship, new Vector3(400, 10));
        SpriteRenderer sr = ship.addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("largestShip");
        Ship comp = ship.addComponent(Ship.class);
        comp.size = 4;
        comp.startPos = new Vector3(400, 10);
        ships[9] = comp;

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

        goButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                if (button == 1) {
                    ready();
                }
            }
        };
        goButton.alignType = Align.CENTER;
        goButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
        goButton.setText("go!");
        goButton.font = FontLoader.getFont("default");
        goButton.setTextOffset(new Vector3(100, 20));
        goButton.fontScale = new Vector3(0.6f, 0.6f);
        goButton.fontColor = new Color(0, 0, 0, 0);
        goButton.color = new Color(255, 255, 255, 0);

        turnTimeLabel = new Label();
        turnTimeLabel.alignType = Align.LEFT_TOP;
        turnTimeLabel.getTransform().setScale(new Vector3(0.8f, 0.8f));
        turnTimeLabel.font = FontLoader.getFont("default");
        turnTimeLabel.sprite = Resources.getSprite("loginPanel");
        turnTimeLabel.setTextOffset(new Vector3(100, 50));
        turnTimeLabel.fontScale = new Vector3(0.6f, 0.6f);
        turnTimeLabel.left = 100;
        turnTimeLabel.top = 10;

        enemyTurnTimeLabel = new Label();
        enemyTurnTimeLabel.alignType = Align.RIGHT_TOP;
        enemyTurnTimeLabel.getTransform().setScale(new Vector3(0.8f, 0.8f));
        enemyTurnTimeLabel.font = FontLoader.getFont("default");
        enemyTurnTimeLabel.sprite = Resources.getSprite("loginPanel");
        enemyTurnTimeLabel.setTextOffset(new Vector3(100, 50));
        enemyTurnTimeLabel.fontScale = new Vector3(0.6f, 0.6f);
        enemyTurnTimeLabel.right = 100;
        enemyTurnTimeLabel.top = 10;

        winnerLabel = new Label();
        winnerLabel.alignType = Align.CENTER;
        winnerLabel.getTransform().setScale(new Vector3(1.5f, 1.5f));
        winnerLabel.font = FontLoader.getFont("default");
        winnerLabel.sprite = Resources.getSprite("loginPanel");
        winnerLabel.setTextOffset(new Vector3(100, 50));
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
        thisNickLabel.setText(Client.current.loggedAs);

        enemyNickLabel = new Label();
        enemyNickLabel.alignType = Align.RIGHT_TOP;
        enemyNickLabel.getTransform().setScale(new Vector3(1.137f, 1.2f));
        enemyNickLabel.font = FontLoader.getFont("default");
        enemyNickLabel.sprite = Resources.getSprite("EXPpanel");
        enemyNickLabel.setTextOffset(new Vector3(145, 115));
        enemyNickLabel.fontScale = new Vector3(0.5f, 0.5f);
        enemyNickLabel.right = 90;
        enemyNickLabel.top = 40;
        enemyNickLabel.setText(Game.current.opponent);

        Rank myRank = Database.getRank(Database.getPlayer(Client.current.loggedAs));

        myRankFrame = new Label();
        myRankFrame.sprite = Resources.getSprite("rangFrame");
        myRankFrame.alignType = Align.LEFT_TOP;
        myRankFrame.left = 110;
        myRankFrame.top = 65;
        myRankFrame.getTransform().setScale(new Vector3(0.5f, 0.5f));

        myRankImage = new Label();
        myRankImage.sprite = Resources.getSprite(myRank.getImage());
        myRankImage.setText(myRank.getRangname());
        myRankImage.alignType = Align.LEFT_TOP;
        myRankImage.left = 110;
        myRankImage.top = 69;
        myRankImage.getTransform().setScale(new Vector3(0.5f, 0.5f));
        myRankImage.setTextOffset(new Vector3(145, 80));
        myRankImage.fontScale = new Vector3(0.3f, 0.3f);

        System.out.println(myRank.getRangname());

        addGUI(goButton);
        addGUI(turnTimeLabel);
        addGUI(enemyTurnTimeLabel);
        addGUI(thisNickLabel);
        addGUI(enemyNickLabel);

        addGUI(myRankFrame);
        addGUI(myRankImage);

        addGUI(winnerLabel);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Vector3 offset = new Vector3(100 + square.getImageWidth() * x, 1080 - 250 - 64 - square.getImageHeight() * y);

                GameObject gm = new GameObject();
                instantiate(gm, offset);

                FieldElement element = gm.addComponent(FieldElement.class);
                element.game = this;
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
                element.game = this;
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

    protected void ready() {
        goButton.fontColor = new Color(0, 0, 0, 0);
        goButton.color = new Color(0, 0, 0, 0);
        isPressReady = true;
        String fieldString = "";
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                fieldString += thisField[x][y].state + ";";
            }
        }

        send("field;" + Client.current.loggedAs + ";" + fieldString);
        send("go;" + Client.current.loggedAs);
    }

    @Override
    protected void update() {
        turnTimeLabel.setText(Math.floor(thisTimer) + "");
        enemyTurnTimeLabel.setText(Math.floor(enemyTimer) + "");

        if (isGameEnd)
            return;

        if (selectedShip != null && !isReady && Input.isKeyDown(KeyEvent.VK_R)) {
            if (selectedShip.rot == 0)
                selectedShip.rot = 1;
            else
                selectedShip.rot = 0;
        }

        boolean allReady = true;
        for (int i = 0; i < ships.length; i++) {
            Ship ship = ships[i];
            if (!ship.isSetup)
                allReady = false;
        }

        if (allReady && !isPressReady) {
            goButton.fontColor = new Color(0, 0, 0, 255);
            goButton.color = new Color(255, 255, 255, 255);
        }

        if (!isReady)
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

        if (thisTimer < 0) {
            next();
        }
    }

    public void next() {
        turn = false;
        thisTimer = 15;
        enemyTimer = 15;
        Client.current.sendMessage("next;" + Client.current.loggedAs);
    }

    public void setupShips() {
        isGameEnd = true;
    }

    public void setTurn() {
        turn = true;
        thisTimer = 15;
        enemyTimer = 15;
    }

    public void end() {
        isGameEnd = true;
        winnerLabel.color = new Color(255, 255, 255, 255);
        winnerLabel.setText(winner + " win!");
    }

    private void send(String msg) {
        Client.current.sendMessage(msg);
    }
}
