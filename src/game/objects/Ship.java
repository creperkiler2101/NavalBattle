package game.objects;

import engine.base.Component;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import game.objects.controllers.GameController;

import java.awt.*;

public class Ship extends Component {
    public int size = -1;
    public int rot = 0;
    public int x = -1, y = -1;
    public boolean isSetup = false;
    public Vector3 startPos;

    public int xOffset = 0, yOffset = 0;

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (isSetup)
            return;

        if (GameController.current.selectedShip != this && startPos != null) {
            getGameObject().getTransform().setPosition(startPos);
            SpriteRenderer sr = getGameObject().getComponents(SpriteRenderer.class)[0];
            sr.color = new Color(255, 255, 255, 255);
        }
        else if (GameController.current.selectedShip == this) {
            SpriteRenderer sr = getGameObject().getComponents(SpriteRenderer.class)[0];
            sr.color = new Color(155, 155, 155, 255);

            if (rot == 0) {
                getGameObject().getTransform().setRotation(new Vector3(0, 0, 0));
            }
            else {
                getGameObject().getTransform().setRotation(new Vector3(-90, 0, 0));
            }
        }
        else {
            SpriteRenderer sr = getGameObject().getComponents(SpriteRenderer.class)[0];
            sr.color = new Color(255, 255, 255, 255);
        }
    }

    @Override
    public void mouseUp(int button) {
        //if (button == 1 && !isSetup) {
        //    GameController.current.selectedShip = null;
        //}
    }

    @Override
    public void mouseDown(int button) {
        if (button == 1 && !isSetup) {
            GameController.current.selectedShip = this;
        }
    }
}
