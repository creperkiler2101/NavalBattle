package game.scenes;

import engine.base.GameObject;
import engine.base.Scene;
import game.objects.login.LoginController;

public class LoginScene extends Scene {

    @Override
    public void init() {
        GameObject loginObject = new GameObject() {
            @Override
            public void init() {
                addComponent(LoginController.class);
            }
        };
        instantiate(loginObject);
    }
}
