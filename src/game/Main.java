package game;

import engine.base.GameObject;
import engine.base.Scene;
import engine.base.Vector3;
import engine.base.components.ParticleSystem;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Resources;
import engine.core.font.FontLoader;
import game.scenes.LoginScene;

public class Main {
    public static void main(String[] args) {
        Application app = new Application() {
            @Override
            public void onGLInitialized() {
                setScene(LoginScene.class);
            }

            @Override
            public void resourceLoad() {
                this.loadResources(Main.class.getResource("resources").toString());
                FontLoader.LoadFont(Main.class.getResource("Font.png").toString().replace("%20", " ").replace("/", "\\").substring(6), "default");
            }
        };
        app.show();
        app.setTitle("Naval Battle");
        //Application.getCurrent().goFullscreen();
    }
}
