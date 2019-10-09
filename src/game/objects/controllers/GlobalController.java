package game.objects.controllers;

import engine.base.Component;
import engine.core.Application;
import engine.core.Input;

import java.awt.event.KeyEvent;

public class GlobalController extends Component {
    @Override
    public void update() {
        if (Input.isKeyDown(KeyEvent.VK_ENTER)) {
            if (Application.getCurrent().isFullscreen)
                Application.getCurrent().goWindowed();
            else
                Application.getCurrent().goFullscreen();
        }
    }
}
