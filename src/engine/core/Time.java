package engine.core;

public class Time {
    protected static float deltaTime;
    public static float getDeltaTime() {
        return deltaTime;
    }
    public static final float startupDelay = 3.12f;

    protected static float fps;
    public static float getFps() {
        return fps;
    }
}
