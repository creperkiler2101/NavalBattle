package game;

import engine.base.GameObject;
import engine.base.Scene;
import engine.base.Vector3;
import engine.base.components.ParticleSystem;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Resources;
import engine.core.font.FontLoader;

public class Main {
    public static void main(String[] args) {
        Application app = new Application() {
            @Override
            public void onGLInitialized() {
                this.loadResources(Main.class.getResource("resources").toString());
                FontLoader.LoadFont(Main.class.getResource("Font.png").toString().replace("%20", " ").replace("/", "\\").substring(6), "default");

                GameObject gm = new GameObject() {
                    @Override
                    public void init() {
                        addComponent(TestComponent.class);
                    }
                };
                gm.getTransform().setPosition(new Vector3(0, 0, 0));

                setScene(Scene.class);
                getCurrentScene().instantiate(gm);
            }
        };
        app.show();
        //Application.getCurrent().goFullscreen();
    }
}
