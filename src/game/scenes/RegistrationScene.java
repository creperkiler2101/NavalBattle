package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.controllers.GlobalController;
import game.objects.controllers.RegistrationController;

import java.awt.event.KeyEvent;

public class RegistrationScene extends Scene {

    @Override
    public void init() {
        GameObject global = new GameObject() {
            @Override
            public void init() {
                addComponent(GlobalController.class);
            }
        };
        instantiate(global);

        GameObject regObject = new GameObject() {
            @Override
            public void init() {
                addComponent(RegistrationController.class);
            }
        };
        instantiate(regObject);
    }

    @Override
    public void keyUp(KeyEvent event) {
        super.keyUp(event);
    }
}
