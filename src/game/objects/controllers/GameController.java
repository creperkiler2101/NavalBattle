package game.objects.controllers;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import engine.core.Time;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Label;
import game.connection.Client;
import game.objects.FieldElement;
import game.objects.ui.MyButton;

import java.awt.*;

public class GameController extends Component {
    public FieldElement[][] thisField = new FieldElement[10][10];
    public FieldElement[][] opponentField = new FieldElement[10][10];
    public boolean turn = false;
    public boolean isReady = false;

    public static GameController current;

    private Texture square = Resources.getSprite("white");

    private MyButton goButton;
    private Label timeLabel, turnTimeLabel, enemyTurnTimeLabel;

    private float thisTimer = 15;
    private float enemyTimer = 15;

    @Override
    protected void start() {
        current = this;
        Client.current.setTurn = this::setTurn;

        GameObject seaBG_1 = new GameObject();
        GameObject seaBG_2 = new GameObject();
        instantiate(seaBG_1, new Vector3(100, 1080 - 100 - 640));
        instantiate(seaBG_2, new Vector3(1920 - 100 - 640, 1080 - 100 - 640));

        SpriteRenderer sr_1 = seaBG_1.addComponent(SpriteRenderer.class);
        sr_1.sprite = Resources.getSprite("sea");

        SpriteRenderer sr_2 = seaBG_2.addComponent(SpriteRenderer.class);
        sr_2.sprite = Resources.getSprite("sea");

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

        addGUI(goButton);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Vector3 offset = new Vector3(100 + square.getImageWidth() * x, 1080 - 100 - 64 - square.getImageHeight() * y);

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
                Vector3 offset = new Vector3(1920 - 100 - 640 + square.getImageWidth() * x, 1080 - 100 - 64 - square.getImageHeight() * y);

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

    protected void ready() {
        goButton.fontColor = new Color(0, 0, 0, 0);
        goButton.color = new Color(0, 0, 0, 0);
        send("go;" + Client.current.loggedAs);
    }

    @Override
    protected void update() {
        if (!isReady)
            return;

        if (turn) {
            thisTimer -= 1 * Time.getDeltaTime();
        }
        else {
            enemyTimer -= 1 * Time.getDeltaTime();
        }

        if (thisTimer == 0) {
            next();
        }
    }

    public void next() {
        turn = false;
        Client.current.sendMessage("next;" + Client.current.loggedAs);
    }

    public void setupShips() {

    }

    public void setTurn() {
        turn = true;
        thisTimer = 15;
        enemyTimer = 15;
    }

    private void send(String msg) {
        Client.current.sendMessage(msg);
    }
}
