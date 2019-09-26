package engine.base;

public class Transform {
    private GameObject gameObject;
    public GameObject getGameObject() {
        return gameObject;
    }

    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    protected Transform(GameObject gameObject) {
        this.position = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1, 1,1);
        this.gameObject = gameObject;
    }
}
