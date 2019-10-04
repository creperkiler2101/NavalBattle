package engine.ui;

import engine.base.Vector3;

public class Button extends UIBase {
    private Vector3 saved;
    private boolean lastState;

    @Override
    public void update() {
        super.update();
        if (isPressed() && !lastState) {
            saved = getTextOffset();
            setTextOffset(new Vector3(saved.x + 5, saved.y - 5));
        }
        else if (!isPressed() && lastState) {
            setTextOffset(saved);
        }

        lastState = isPressed();
    }
}
