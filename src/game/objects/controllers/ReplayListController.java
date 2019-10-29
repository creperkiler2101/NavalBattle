package game.objects.controllers;

import engine.base.Camera;
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
import game.objects.ReplayList;
import game.objects.ui.MyButton;
import game.scenes.MainScene;

import java.awt.event.KeyEvent;

public class ReplayListController extends Component {
    public float listScroll = 0;
    public ReplayList replayList;

    private Label sKeyL, wKeyL;

    @Override
    protected void start() {
        replayList = getGameObject().addComponent(ReplayList.class);
        replayList.games = Database.getGames(Client.current.loggedAs);
        replayList.drawGUI();

        MyButton exitButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                if (button == 1)
                    Application.getCurrent().setScene(MainScene.class);
            }
        };
        exitButton.alignType = Align.RIGHT;
        exitButton.getTransform().setScale(new Vector3(1.2f, 1.2f));
        exitButton.setText("exit");
        exitButton.font = FontLoader.getFont("default");
        exitButton.setTextOffset(new Vector3(70, 20));
        exitButton.fontScale = new Vector3(0.6f, 0.6f);
        exitButton.right = 100;

        sKeyL = new Label();
        sKeyL.font = FontLoader.getFont("default");
        sKeyL.sprite = Resources.getSprite("SKey");
        sKeyL.setText(" - scroll down");
        sKeyL.alignType = Align.RIGHT_BOTTOM;
        sKeyL.setTextOffset(new Vector3(60, 16));
        sKeyL.right = 500;
        sKeyL.bottom = -50 + 150;
        sKeyL.fontScale = new Vector3(0.4f, 0.4f);
        sKeyL.getTransform().setScale(new Vector3(1.8f, 1.8f));

        wKeyL = new Label();
        wKeyL.font = FontLoader.getFont("default");
        wKeyL.sprite = Resources.getSprite("WKey");
        wKeyL.setText(" - scroll up");
        wKeyL.alignType = Align.RIGHT_BOTTOM;
        wKeyL.setTextOffset(new Vector3(60, 16));
        wKeyL.right = 500;
        wKeyL.bottom = 50 + 150;
        wKeyL.fontScale = new Vector3(0.4f, 0.4f);
        wKeyL.getTransform().setScale(new Vector3(1.8f, 1.8f));

        Label frame = new Label();
        frame.getTransform().setScale(new Vector3(2f, 2));
        frame.alignType = Align.RIGHT_BOTTOM;
        frame.right = -60;
        frame.bottom = -60;
        frame.sprite = Resources.getSprite("loginPanel");

        addGUI(frame);
        addGUI(exitButton);
        addGUI(sKeyL);
        addGUI(wKeyL);

        GameObject gm = new GameObject();
        instantiate(gm);
        SpriteRenderer sr = gm.addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        gm.getTransform().setScale(new Vector3(2.6f,2.6f));
    }

    @Override
    protected void update() {
        if (Input.isKeyPress(KeyEvent.VK_S))
            listScroll += 500 * Time.getDeltaTime();

        if (Input.isKeyPress(KeyEvent.VK_W))
            listScroll -= 500 * Time.getDeltaTime();

        if (listScroll > 40 + replayList.games.size() * 200 + replayList.games.size() * 50 - 1080)
            listScroll = 40 + replayList.games.size() * 200 + replayList.games.size() * 50 - 1080;
        if (listScroll < 0)
            listScroll = 0;

        replayList.getGameObject().getTransform().setPosition(new Vector3(0, listScroll));
        //System.out.println(listScroll + " " + (40 + 200 + replayList.games.size() * 200 + replayList.games.size() * 50));
    }
}
