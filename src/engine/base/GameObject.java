package engine.base;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class GameObject {
    protected ArrayList<Component> components;

    protected Transform transform;
    public Transform getTransform() {
        return transform;
    }

    public String tag;
    public String name;

    public boolean isEnabled;

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
}
