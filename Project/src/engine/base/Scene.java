package engine.base;

import engine.base.components.SpriteRenderer;
import engine.core.Input;
import engine.ui.UIBase;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Scene {
    protected ArrayList<GameObject> gameObjects;
    protected ArrayList<GameObject> toDestroy;
    protected ArrayList<DelayedMethod> delayExecute;
    protected ArrayList<GameObject> mouseEnterObjects;

    protected ArrayList<UIBase> GUI;
    protected ArrayList<UIBase> toDestoryGUI;
    protected ArrayList<UIBase> mouseEnterGUI;

    public boolean isReady;

    public Scene() {
        this.isReady = false;
        this.gameObjects = new ArrayList<GameObject>();
        this.toDestroy = new ArrayList<GameObject>();
        this.delayExecute = new ArrayList<DelayedMethod>();
        this.mouseEnterObjects = new ArrayList<GameObject>();
        this.GUI = new ArrayList<UIBase>();
        this.toDestoryGUI = new ArrayList<UIBase>();
        this.mouseEnterGUI = new ArrayList<UIBase>();
    }

    public void init() { }
    public void update() {
        try {
            for (int i = 0; i < toDestroy.size(); i++) {
                GameObject gm_1 = toDestroy.get(i);
                for (int _i = 0; _i < gameObjects.size(); _i++) {
                    GameObject gm_2 = gameObjects.get(_i);
                    if (gm_1 == gm_2) {
                        gameObjects.remove(gm_1);
                        break;
                    }
                }
                for (int _i = 0; _i < delayExecute.size(); _i++) {
                    if (delayExecute.get(_i).gameObject == toDestroy.get(i)) {
                        delayExecute.remove(_i);
                    }
                }
            }

            for (int i = 0; i < toDestoryGUI.size(); i++) {
                UIBase gm_1 = toDestoryGUI.get(i);
                for (int _i = 0; _i < GUI.size(); _i++) {
                    UIBase gm_2 = GUI.get(_i);
                    if (gm_1 == gm_2) {
                        GUI.remove(gm_1);
                        break;
                    }
                }
            }

            toDestroy.clear();
            toDestoryGUI.clear();

            for (int i = 0; i < delayExecute.size(); i++) {
                DelayedMethod dm = delayExecute.get(i);
                boolean result = dm.tryExecute();
                if (result) {
                    delayExecute.remove(i);
                    i--;
                }
            }

            if (Input.isButtonDown(MouseEvent.BUTTON1))
                mouseDown(MouseEvent.BUTTON1);
            if (Input.isButtonDown(MouseEvent.BUTTON2))
                mouseDown(MouseEvent.BUTTON2);
            if (Input.isButtonDown(MouseEvent.BUTTON3))
                mouseDown(MouseEvent.BUTTON3);

            if (Input.isButtonPress(MouseEvent.BUTTON1))
                mousePress(MouseEvent.BUTTON1);
            if (Input.isButtonPress(MouseEvent.BUTTON2))
                mousePress(MouseEvent.BUTTON2);
            if (Input.isButtonPress(MouseEvent.BUTTON3))
                mousePress(MouseEvent.BUTTON3);

            if (Input.isButtonUp(MouseEvent.BUTTON1))
                mouseUp(MouseEvent.BUTTON1);
            if (Input.isButtonUp(MouseEvent.BUTTON2))
                mouseUp(MouseEvent.BUTTON2);
            if (Input.isButtonUp(MouseEvent.BUTTON3))
                mouseUp(MouseEvent.BUTTON3);

            for (int i = 0; i < gameObjects.size(); i++)
                if (gameObjects.get(i).isEnabled)
                    gameObjects.get(i).update();

            for (int i = 0; i < GUI.size(); i++)
                GUI.get(i).update();
        }
        catch (Exception ex) { }
    }

    public void addGUI(UIBase element) {
        GUI.add(element);
        element.start();
    }

    public void destoryGUI(UIBase element) {
        toDestoryGUI.add(element);
    }

    public void mouseMove() {
        Vector3 mousePosition = Input.getMousePosition();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gm = gameObjects.get(i);
            Bounds bounds = gm.getBounds();
            if (bounds != null) {
                if (bounds.isInBounds(mousePosition)) {
                    if (!mouseEnterObjects.contains(gm)) {
                        mouseEnterObjects.add(gm);
                        gm.mouseEnter();
                    }
                    else {
                        gm.mouseMove();
                    }
                }
                else if (!bounds.isInBounds(mousePosition) && mouseEnterObjects.contains(gm)) {
                    mouseEnterObjects.remove(gm);
                    gm.mouseExit();
                }
            }
        }

        for (int i = 0; i < GUI.size(); i++) {
            UIBase gm = GUI.get(i);
            Bounds bounds = gm.getBounds();
            if (bounds != null) {
                if (bounds.isInBounds(mousePosition)) {
                    if (!mouseEnterGUI.contains(gm)) {
                        mouseEnterGUI.add(gm);
                        gm.mouseEnter();
                    }
                    else {
                        gm.mouseMove();
                    }
                }
                else if (!bounds.isInBounds(mousePosition) && mouseEnterGUI.contains(gm)) {
                    mouseEnterGUI.remove(gm);
                    gm.mouseExit();
                }
            }
        }
    }

    public void mouseDown(int button) {
        for (int i = 0; i < mouseEnterObjects.size(); i++) {
            mouseEnterObjects.get(i).mouseDown(button);
        }

        for (int i = 0; i < mouseEnterGUI.size(); i++) {
            mouseEnterGUI.get(i).mouseDown(button);
        }
    }
    public void mousePress(int button) {
        for (int i = 0; i < mouseEnterObjects.size(); i++) {
            mouseEnterObjects.get(i).mousePress(button);
        }

        for (int i = 0; i < mouseEnterGUI.size(); i++) {
            mouseEnterGUI.get(i).mousePress(button);
        }
    }
    public void mouseUp(int button) {
        for (int i = 0; i < mouseEnterObjects.size(); i++) {
            mouseEnterObjects.get(i).mouseUp(button);
        }

        for (int i = 0; i < mouseEnterGUI.size(); i++) {
            mouseEnterGUI.get(i).mouseUp(button);
        }
    }

    public void keyPress(KeyEvent event) {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).keyPress(event);
        }

        for (int i = 0; i < GUI.size(); i++) {
            GUI.get(i).keyPress(event);
        }
    }

    public void keyUp(KeyEvent event) {
        for (int i = 0; i < gameObjects.size(); i++) {
            gameObjects.get(i).keyUp(event);
        }

        for (int i = 0; i < GUI.size(); i++) {
            GUI.get(i).keyUp(event);
        }
    }

    public GameObject[] getGameObjectByTag(String tag) {
        ArrayList<GameObject> items = new ArrayList<GameObject>();
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).tag.equals(tag))
                items.add(gameObjects.get(i));
        }
        return items.toArray(GameObject[]::new);
    }

    public GameObject[] getGameObjectByName(String name) {
        ArrayList<GameObject> items = new ArrayList<GameObject>();
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i).name.equals(name))
                items.add(gameObjects.get(i));
        }
        return items.toArray(GameObject[]::new);
    }

    public void delayedExecute(Runnable method, float seconds, GameObject sender) {
        DelayedMethod dm = new DelayedMethod();
        dm.method = method;
        dm.seconds = seconds;
        dm.gameObject = sender;

        delayExecute.add(dm);
    }

    public void instantiate(GameObject gm) {
        gameObjects.add(gm);
        gm.init();
    }

    public void destroy(GameObject gameObject) {
        toDestroy.add(gameObject);
    }
}
