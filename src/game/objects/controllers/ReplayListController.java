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
import game.connection.Client;
import game.database.Database;
import game.objects.ReplayList;
import game.objects.ui.MyButton;
import game.scenes.MainScene;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ReplayListController extends Component {
    public float listScroll = 0;
    public ReplayList replayList;

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
        exitButton.setTextOffset(new Vector3(80, 20));
        exitButton.fontScale = new Vector3(0.6f, 0.6f);
        exitButton.right = 100;

        addGUI(exitButton);

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
        else if (listScroll < 0)
            listScroll = 0;

        replayList.getGameObject().getTransform().setPosition(new Vector3(0, listScroll));
        System.out.println(listScroll + " " + (40 + 200 + replayList.games.size() * 200 + replayList.games.size() * 50));
    }
}
