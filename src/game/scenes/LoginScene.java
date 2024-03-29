package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.controllers.GlobalController;
import game.objects.controllers.LoginController;

public class LoginScene extends Scene {

    @Override
    public void init() {
        GameObject global = new GameObject() {
            @Override
            public void init() {
                addComponent(GlobalController.class);
            }
        };
        instantiate(global);

        GameObject loginObject = new GameObject() {
            @Override
            public void init() {
                addComponent(LoginController.class);
            }
        };
        instantiate(loginObject);
    }
}
