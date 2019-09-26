package engine.base;

import engine.core.Time;

class DelayedMethod {
    protected Runnable method;
    protected float seconds;
    protected float i = 0;
    protected GameObject gameObject;

    protected boolean tryExecute() {
        i += 1 * Time.getDeltaTime();
        if (i >= seconds) {
            method.run();
            return true;
        }
        return false;
    }
}
