package engine.base;

public final class Transform {
    private GameObject gameObject;
    public GameObject parent;
    public GameObject getGameObject() {
        return gameObject;
    }

    protected Vector3 position;
    protected Vector3 rotation;
    protected Vector3 scale;

    //Only this position
    public Vector3 getLocalPosition() {
        return position;
    }
    public Vector3 getLocalRotation() {
        return rotation;
    }
    public Vector3 getLocalScale() {
        return scale;
    }

    //With parent position
    public Vector3 getPosition() {
        if (parent != null)
            return new Vector3(parent.getTransform().getPosition().x + position.x, parent.getTransform().getPosition().y + position.y, parent.getTransform().getPosition().z + position.z);
        return position;
    }
    public Vector3 getRotation() {
        if (parent != null)
            return new Vector3(parent.getTransform().getRotation().x + rotation.x, parent.getTransform().getRotation().y + rotation.y, parent.getTransform().getRotation().z + rotation.z);
        return rotation;
    }
    public Vector3 getScale() {
        if (parent != null)
            return new Vector3(parent.getTransform().getScale().x + scale.x, parent.getTransform().getScale().y + scale.y, parent.getTransform().getScale().z + scale.z);
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
