package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.controllers.GlobalController;
import game.objects.controllers.MainController;

public class MainScene extends Scene {

    @Override
    public void init() {
        GameObject global = new GameObject() {
            @Override
            public void init() {
                addComponent(GlobalController.class);
                addComponent(MainController.class);
            }
        };
        instantiate(global);
    }
}
