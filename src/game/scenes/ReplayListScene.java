package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.controllers.GlobalController;
import game.objects.controllers.RegistrationController;
import game.objects.controllers.ReplayListController;

public class ReplayListScene extends Scene {
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
                addComponent(ReplayListController.class);
            }
        };
        instantiate(regObject);
    }
}
