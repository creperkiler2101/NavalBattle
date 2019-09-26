package game;

import engine.base.GameObject;
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

        GameObject gm = new GameObject();
        //boolean result = gm.addComponent(TestComponent.class);
        gm.addComponent(TestComponentA.class);
        gm.addComponent(TestComponentA.class);
        gm.addComponent(TestComponentA.class);
        gm.addComponent(TestComponentA.class);
        gm.addComponent(TestComponentA.class);

       // System.out.println(result);
        //gm.removeComponents(TestComponent.class);

        //TestComponent comp = gm.getComponents(TestComponent.class)[0];
        //System.out.println(comp.getClass().getName());
        //System.out.println(gm.components.size());
    }
}
