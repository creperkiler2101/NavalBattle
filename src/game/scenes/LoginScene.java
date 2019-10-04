package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.TestComponent;
import game.objects.login.GlobalController;
import game.objects.login.LoginController;

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
