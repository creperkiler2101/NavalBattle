package game.objects.controllers;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.core.Resources;
import game.objects.FieldElement;

public class GameController extends Component {
    public FieldElement[][] thisField = new FieldElement[10][10];
    public FieldElement[][] opponentField = new FieldElement[10][10];
    public boolean turn = false;

    private Texture square = Resources.getSprite("square");

    @Override
    protected void start() {
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                Vector3 offset = new Vector3(100 + square.getImageWidth() * x, 1080 - 100 - square.getImageHeight() * y);

                GameObject gm = new GameObject();
                instantiate(gm, offset);

                FieldElement element = gm.addComponent(FieldElement.class);
                element.x = x;
                element.y = y;
                element.isCurrent = true;
            }
        }
    }

    @Override
    protected void update() {

    }

    public void setupShips() {

    }

    public void setTurn() {
        turn = true;
    }
}
