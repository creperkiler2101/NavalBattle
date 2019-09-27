package game;

import engine.base.GameObject;
import engine.base.Scene;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Resources;

public class Main {
    public static void main(String[] args) {
        Application app = new Application() {
            @Override
            public void onGLInitialized() {
                this.loadResources(Main.class.getResource("resources").toString());

                GameObject gm = new GameObject() {
                    @Override
                    public void init() {
                        addComponent(TestComponent.class);
                    }
                };

                setScene(Scene.class);
                getCurrentScene().instantiate(gm);

            }
        };
        app.show();
        //Application.getCurrent().goFullscreen();
    }
}
