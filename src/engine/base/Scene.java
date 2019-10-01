package engine.base;

import engine.base.components.SpriteRenderer;
import engine.core.Input;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Scene {
    protected ArrayList<GameObject> gameObjects;
    protected ArrayList<GameObject> toDestroy;
    protected ArrayList<DelayedMethod> delayExecute;
    protected ArrayList<GameObject> mouseEnterObjects;

    public boolean isReady;

    public Scene() {
        this.isReady = false;
        this.gameObjects = new ArrayList<GameObject>();
        this.toDestroy = new ArrayList<GameObject>();
        this.delayExecute = new ArrayList<DelayedMethod>();
        this.mouseEnterObjects = new ArrayList<GameObject>();
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
        }
        catch (Exception ex) { }
    }

    public void mouseMove() {
        Vector3 mousePosition = Input.getMousePosition();
        ArrayList<SpriteRenderer> toCheck = new ArrayList<SpriteRenderer>();

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
    }
    public void mouseDown(int button) {
        for (int i = 0; i < mouseEnterObjects.size(); i++) {
            mouseEnterObjects.get(i).mouseDown(button);
        }
    }
    public void mousePress(int button) {
        for (int i = 0; i < mouseEnterObjects.size(); i++) {
            mouseEnterObjects.get(i).mousePress(button);
        }
    }
    public void mouseUp(int button) {
        for (int i = 0; i < mouseEnterObjects.size(); i++) {
            mouseEnterObjects.get(i).mouseUp(button);
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
