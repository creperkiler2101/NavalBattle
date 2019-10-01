package engine.base;

import engine.core.Application;

public class Component {
    GameObject gameObject;
    public GameObject getGameObject() {
        return gameObject;
    }

    public GameObject[] findByName(String name) {
        return Application.getCurrent().getCurrentScene().getGameObjectByName(name);
    }
    public GameObject[] findByTag(String tag) {
        return Application.getCurrent().getCurrentScene().getGameObjectByTag(tag);
    }

    public void destroy(GameObject gameObject) {
        Application.getCurrent().getCurrentScene().destroy(gameObject);
    }

    public void delayedExecute(Runnable method, float delay) {
        Application.getCurrent().getCurrentScene().delayedExecute(method, delay, gameObject);
    }

    public void instantiate(GameObject gameObject) {
        Application.getCurrent().getCurrentScene().instantiate(gameObject);
    }
    public void instantiate(GameObject gameObject, Vector3 position) {
        gameObject.getTransform().setPosition(position);

        Application.getCurrent().getCurrentScene().instantiate(gameObject);
    }
    public void instantiate(GameObject gameObject, Vector3 position, Vector3 rotation) {
        gameObject.getTransform().setPosition(position);
        gameObject.getTransform().setRotation(rotation);

        Application.getCurrent().getCurrentScene().instantiate(gameObject);
    }
    public void instantiate(GameObject gameObject, Vector3 position, Vector3 rotation, Vector3 scale) {
        gameObject.getTransform().setPosition(position);
        gameObject.getTransform().setRotation(rotation);
        gameObject.getTransform().setScale(scale);

        Application.getCurrent().getCurrentScene().instantiate(gameObject);
    }

    protected Component() { }
    protected void start() { }
    protected void update() { }

    protected void mouseEnter() { }
    protected void mouseMove() { }
    protected void mouseExit() { }

    protected void mouseDown(int button) { }
    protected void mousePress(int button) { }
    protected void mouseUp(int button) { }
}
