package engine.base;

public class Bounds {
    public float left;
    public float right;
    public float top;
    public float bottom;

    public boolean isInBounds(Vector3 point) {
        return (point.x >= left && point.x <= right &&
                point.y >= bottom && point.y <= top);
    }
}
