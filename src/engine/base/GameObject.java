package engine.base;

import engine.base.components.SpriteRenderer;
import engine.ui.UIBase;

import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameObject {
    protected ArrayList<Component> components;

    protected Transform transform;
    public Transform getTransform() {
        return transform;
    }

    public String tag;
    public String name;

    public boolean isEnabled;

    public Bounds getBounds() {
        SpriteRenderer[] array = getComponents(SpriteRenderer.class);
        if (array.length == 0)
            return null;
        SpriteRenderer sr = array[0];
        if (sr.sprite == null)
            return null;

        Bounds result = new Bounds();

        //MB errors here
        result.left = getTransform().getPosition().x;
        result.right = (result.left + sr.sprite.getImageWidth()) * getTransform().getScale().x;

        result.bottom = getTransform().getPosition().y;
        result.top = (result.bottom + sr.sprite.getImageHeight()) * getTransform().getScale().y;

        return result;
    }

    public GameObject() {
        this.components = new ArrayList<Component>();
        this.transform = new Transform(this);
        this.tag = "";
        this.name = "";
        this.isEnabled = true;
    }

    public void init() { }
    public void update() {
        for (int i = 0; i < components.size(); i++)
            components.get(i).update();
    }

    //Если компонент добавлен - true, иначе - false
    public <T extends Component> T addComponent(Class<T> type) {
        try {
            Component c = (Component)type.getConstructors()[0].newInstance();
            c.gameObject = this;
            components.add(c);
            c.start();
            return (T)c;
        }
        catch (Exception ex) {
            System.out.println("Component of type " + type.getName() + " not added to " + name);
            return null;
        }
    }

    public <T extends Component> void removeComponents(Class<T> type) {
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getClass().isNestmateOf(type)) {
                components.remove(i);
                i--;
            }
        }
    }

    public <T extends Component> T[] getComponents(Class<T> type) {
        ArrayList<T> result = new ArrayList<T>();
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getClass().isNestmateOf(type)) {
                result.add((T) components.get(i));
            }
        }
        T[] res = (T[]) Array.newInstance(type, result.size());;
        return result.toArray(res);
    }

    protected void mouseEnter() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).mouseEnter();
        }
    }
    protected void mouseExit() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).mouseExit();
        }
    }
    protected void mouseDown(int button) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).mouseDown(button);
        }
    }
    protected void mousePress(int button) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).mousePress(button);
        }
    }
    protected void mouseUp(int button) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).mouseUp(button);
        }
    }
    protected void mouseMove() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).mouseMove();
        }
    }

    protected void keyPress(KeyEvent event) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).keyPress(event);
        }
    }
    protected void keyUp(KeyEvent event) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).keyUp(event);
        }
    }
}
