package engine.core;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public final class Input {
    protected static ArrayList<Integer> keyPress = new ArrayList<Integer>();
    protected static ArrayList<Integer> keyDown = new ArrayList<Integer>();
    protected static ArrayList<Integer> keyUp = new ArrayList<Integer>();

    public static boolean isKeyDown(int keyCode) {
        return keyDown.contains(keyCode);
    }

    public static boolean isKeyPress(int keyCode) {
        return keyPress.contains(keyCode);
    }

    public static boolean isKeyUp(int keyCode) {
        return keyUp.contains(keyCode);
    }

    protected static void update() {
        for (int i = 0; i < keyDown.size(); i++) {
            keyPress.add(keyDown.get(i));
            keyDown.remove(i);
            i--;
        }
        keyUp.clear();
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
