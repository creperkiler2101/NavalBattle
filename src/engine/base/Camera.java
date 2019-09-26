package engine.base;

public final class Camera {
    public final int width = 1920;
    public final int height = 1080;

    protected static Camera currentActive;
    public static Camera getActiveCamera() {
        return currentActive;
    }
    public static void setActiveCamera(Camera camera) {
        if (camera != null)
            currentActive = camera;
    }

    protected Transform transform;
    public Transform getTransform() {
        return transform;
    }

    public Camera() {
        transform = new Transform();
    }
}
