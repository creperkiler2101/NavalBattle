package game;

import engine.base.Camera;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.core.Input;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import engine.core.Time;
import engine.core.font.Font;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Entry;
import engine.ui.Label;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TestComponent extends Component {
    SpriteRenderer sr;
    Entry entry;

    @Override
    public void start() {
        sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("LeftHand");
        delayedExecute(this::goTop, 5 + Time.startupDelay);
/*
        ParticleSystem ps = getGameObject().addComponent(ParticleSystem.class);
        ps.spawnDelay = 0f;
        ps.sprites.add(Resources.getSprite("blast1"));
        ps.sprites.add(Resources.getSprite("blast2"));
        ps.minStartSpeed = new Vector3(-50, -50, 0);
        ps.maxStartSpeed = new Vector3(50,50, 0);
        ps.minEndSpeed = new Vector3();
        ps.maxEndSpeed = new Vector3();
        */

        GameObject gm = new GameObject();
        gm.getTransform().setPosition(new Vector3(250, 250));
        gm.getTransform().setScale(new Vector3(4, 4));
        instantiate(gm);
        entry = new Entry();
        entry.sprite = Resources.getSprite("square");
        entry.font = FontLoader.getFont("default");
        entry.getTransform().setScale(new Vector3(5,5,5));
        addGUI(entry);

        getGameObject().getTransform().setScale(new Vector3(5,5,5));
    }

    @Override
    public void update() {
        if (Input.isKeyDown(KeyEvent.VK_E))
            System.out.println("down");
        if (Input.isKeyPress(KeyEvent.VK_E))
            System.out.println("press");
        if (Input.isKeyUp(KeyEvent.VK_E))
            System.out.println("up");
        //Vector3 mp = Input.getMousePosition();



        if (Input.isKeyPress(KeyEvent.VK_W))
            Camera.getActiveCamera().getTransform().getLocalPosition().y += 200 * Time.getDeltaTime();
        if (Input.isKeyPress(KeyEvent.VK_A))
            Camera.getActiveCamera().getTransform().getLocalPosition().x -= 200 * Time.getDeltaTime();
        if (Input.isKeyPress(KeyEvent.VK_D))
            Camera.getActiveCamera().getTransform().getLocalPosition().x += 200 * Time.getDeltaTime();
        if (Input.isKeyPress(KeyEvent.VK_S))
            Camera.getActiveCamera().getTransform().getLocalPosition().y -= 200 * Time.getDeltaTime();

        Vector3 mouse = Input.getMousePosition();
        //System.out.println(mouse.x + " " + mouse.y);

        Font font = FontLoader.getFont("default");
        font.drawString("Typo:da", new Vector3(100, 100), new Vector3(3,3,3), Color.YELLOW, 10);
        //System.out.println(mp.x + " " + mp.y);
        //Camera.getActiveCamera().getTransform().getPosition().x--;
        //Camera.getActiveCamera().getTransform().getPosition().y--;
        /*if (Input.isButtonDown(MouseEvent.BUTTON1))
            System.out.println("Left mouse down");
        if (Input.isButtonPress(MouseEvent.BUTTON1))
            System.out.println("Left mouse press");
        if (Input.isButtonUp(MouseEvent.BUTTON1))
            System.out.println("Left mouse up");*/
        //getGameObject().getTransform().getLocalPosition().x += 1 * Time.getDeltaTime();
    }

    public void goTop() {
        //getGameObject().getTransform().getLocalPosition().y += 100;
        delayedExecute(this::goTop, 5);
    }

    @Override
    protected void mouseEnter() {
        System.out.println("enter");
    }
    @Override
    protected void mouseMove() {
        //System.out.println("move");
    }
    @Override
    protected void mouseExit() {
        System.out.println("exit");
    }

    @Override
    protected void mouseDown(int button) {
        System.out.println("mouse down " + button);
    }
    @Override
    protected void mousePress(int button) {
        System.out.println("mouse press " + button);
    }
    @Override
    protected void mouseUp(int button) {
        System.out.println("mouse up " + button);
    }
}
