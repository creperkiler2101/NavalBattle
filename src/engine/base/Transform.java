package engine.base;

public final class Transform {
    private GameObject gameObject;
    public GameObject getGameObject() {
        return gameObject;
    }

    protected Vector3 position;
    protected Vector3 rotation;
    protected Vector3 scale;

    public Vector3 getPosition() {
        return position;
    }
    public Vector3 getRotation() {
        return rotation;
    }
    public Vector3 getScale() {
        return scale;
    }

    public void setPosition(Vector3 position) {
        if (position != null)
            this.position = position;
    }
    public void setRotation(Vector3 rotation) {
        if (rotation != null)
            this.rotation = rotation;
    }
    public void setScale(Vector3 scale) {
        if (scale != null)
            this.scale = scale;
    }

    protected Transform() {
        this.position = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1, 1,1);
    }

    protected Transform(GameObject gameObject) {
        this.position = new Vector3(0, 0, 0);
        this.rotation = new Vector3(0, 0, 0);
        this.scale = new Vector3(1, 1,1);
        this.gameObject = gameObject;
    }
}
