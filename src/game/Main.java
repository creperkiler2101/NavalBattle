package game;

import engine.core.Application;
import engine.core.Resources;

public class Main {
    public static void main(String[] args) {
        Application app = new Application() {
            @Override
            public void onGLInitialized() {
                this.loadResources(Main.class.getResource("resources").toString());
            }
        };
        app.show();
    }
}
