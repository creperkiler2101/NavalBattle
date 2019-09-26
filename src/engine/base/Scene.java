package engine.base;

import java.util.ArrayList;

public class Scene {
    protected ArrayList<GameObject> gameObjects;
    protected ArrayList<GameObject> toDestroy;
    protected ArrayList<DelayedMethod> delayExecute;

    public boolean isReady;

    public Scene() {
        this.isReady = false;
        this.gameObjects = new ArrayList<GameObject>();
        this.toDestroy = new ArrayList<GameObject>();
        this.delayExecute = new ArrayList<DelayedMethod>();
    }

    public void init() { }
    public void update() {
        for (int i = 0; i < toDestroy.size(); i++) {
            for (int _i = 0; _i < gameObjects.size(); _i++)
            {
                if (gameObjects.get(_i) == toDestroy.get(i))
                {
                    gameObjects.remove(i);
                    break;
                }
            }
            for (int _i = 0; _i < delayExecute.size(); _i++)
            {
                if (delayExecute.get(_i).gameObject == toDestroy.get(i))
                {
                    delayExecute.remove(_i);
                }
            }
        }

        for (int i = 0; i < delayExecute.size(); i++) {
            DelayedMethod dm = delayExecute.get(i);
            boolean result = dm.tryExecute();
            if (result)
            {
                delayExecute.remove(i);
                i--;
            }
        }

        for (int i = 0; i < gameObjects.size(); i++)
            if (gameObjects.get(i).isEnabled)
                gameObjects.get(i).update();
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
