package engine.base;

public class Component {
    GameObject gameObject;
    public GameObject getGameObject() {
        return gameObject;
    }

    protected Component() { }
    protected void start() { }
    protected void update() { }
}
