package engine.core;

import engine.base.Camera;
import engine.base.Vector3;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public final class Input {
    protected static ArrayList<Integer> keyPress = new ArrayList<Integer>();
    protected static ArrayList<Integer> keyDown = new ArrayList<Integer>();
    protected static ArrayList<Integer> keyUp = new ArrayList<Integer>();

    protected static ArrayList<Integer> buttonPress = new ArrayList<Integer>();
    protected static ArrayList<Integer> buttonDown = new ArrayList<Integer>();
    protected static ArrayList<Integer> buttonUp = new ArrayList<Integer>();

    protected static Vector3 mousePosition = new Vector3();
    public static Vector3 getMousePosition() {
        return new Vector3((mousePosition.x / ((Application.getCurrent().getWindowWidth() - 17) / 1920)) + Camera.getActiveCamera().getTransform().getPosition().x, -((mousePosition.y - Application.getCurrent().getWindowHeight()) + 40) / ((Application.getCurrent().getWindowHeight() - 40) / 1080) + Camera.getActiveCamera().getTransform().getPosition().y);
    }

    public static boolean isKeyDown(int keyCode) {
        return keyDown.contains(keyCode);
    }
    public static boolean isKeyPress(int keyCode) {
        return keyPress.contains(keyCode);
    }
    public static boolean isKeyUp(int keyCode) {
        return keyUp.contains(keyCode);
    }

    public static boolean isButtonDown(int buttonCode) {
        return buttonDown.contains(buttonCode);
    }
    public static boolean isButtonPress(int buttonCode) {
        return buttonPress.contains(buttonCode);
    }
    public static boolean isButtonUp(int buttonCode) {
        return buttonUp.contains(buttonCode);
    }

    protected static void update() {
        for (int i = 0; i < keyDown.size(); i++) {
            keyPress.add(keyDown.get(i));
            keyDown.remove(i);
            i--;
        }
        keyUp.clear();

        for (int i = 0; i < buttonDown.size(); i++) {
            buttonPress.add(buttonDown.get(i));
            buttonDown.remove(i);
            i--;
        }
        buttonUp.clear();
    }

    protected static void onButtonPress(MouseEvent e) {
        if (!buttonPress.contains(e.getButton())) {
            if (!buttonDown.contains(e.getButton())) {
                buttonDown.add(e.getButton());
            }
            else {
                buttonPress.add(e.getButton());
            }
        }
    }
    protected static void onButtonUp(MouseEvent e) {
        buttonDown.remove((Object)e.getButton());
        buttonPress.remove((Object)e.getButton());
        buttonUp.add(e.getButton());
    }

    protected static void onKeyPress(KeyEvent e) {
        if (!keyPress.contains(e.getKeyCode())) {
            if (!keyDown.contains(e.getKeyCode())) {
                keyDown.add(e.getKeyCode());
            }
            else {
                keyPress.add(e.getKeyCode());
            }
        }
    }
    protected static void onKeyUp(KeyEvent e) {
        keyPress.remove((Object)e.getKeyCode());
        keyDown.remove((Object)e.getKeyCode());
        keyUp.add(e.getKeyCode());
    }
}
