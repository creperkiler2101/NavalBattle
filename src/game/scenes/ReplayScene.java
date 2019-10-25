package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.ReplayGame;
import game.objects.controllers.GameController;
import game.objects.controllers.GlobalController;
import game.objects.controllers.ReplayController;
import game.objects.controllers.ReplayListController;

public class ReplayScene extends Scene {
    @Override
    public void init() {
        GameObject global = new GameObject() {
            @Override
            public void init() {
                addComponent(GlobalController.class);
                addComponent(ReplayController.class);
            }
        };
        instantiate(global);
    }
}
