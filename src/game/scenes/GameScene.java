package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.controllers.GameController;
import game.objects.controllers.GlobalController;

public class GameScene extends Scene {
    @Override
    public void init() {
        GameObject global = new GameObject() {
            @Override
            public void init() {
                addComponent(GlobalController.class);
                addComponent(GameController.class);
            }
        };
        instantiate(global);
    }
}
