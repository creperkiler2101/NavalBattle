package game.objects;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import game.connection.Client;
import game.objects.controllers.GameController;

import java.awt.*;
import java.util.ArrayList;
import java.util.Currency;

public class FieldElement extends Component {
    public int state;
    public int x;
    public int y;
    public boolean isCurrent = false;

    public SpriteRenderer overlaySprite;
    public SpriteRenderer overlayHitSprite;
    public GameController game;

    public Texture square = Resources.getSprite("white");

    @Override
    public void start() {
        overlaySprite = getGameObject().addComponent(SpriteRenderer.class);
        overlayHitSprite = getGameObject().addComponent(SpriteRenderer.class);

        overlaySprite.sprite = square;
        overlayHitSprite.sprite = square;
        overlaySprite.color = new Color(0, 0, 0, 0);
        overlayHitSprite.color = new Color(0, 0, 0, 0);
    }

    @Override
    public void update() {
        if (state == 1) {
            overlayHitSprite.color = new Color(255, 255, 255, 100);
        }
        else if (state == 2) {
            overlayHitSprite.color = new Color(255, 0, 0, 100);
        }

        if (ship == null)
            return;


        Vector3 placeOffset = new Vector3();
        int xOffset = 0;
        int yOffset = 0;
        boolean canSetup = true;
        if (ship.rot == 0) {
            for (int i = 0; i < ship.size; i++) {
                if (x + i >= 10)
                {
                    xOffset++;
                    placeOffset.x += 64;
                }
            }
        }
        else {
            for (int i = 0; i < ship.size; i++) {
                if (y + i >= 10)
                {
                    yOffset++;
                    placeOffset.y += 64;
                }
            }
        }

        if (canSetup) {
            Vector3 pos = getGameObject().getTransform().getPosition();
            ship.xOffset = xOffset;
            ship.yOffset = yOffset;
            ship.x = x;
            ship.y = y;
            if (ship.rot == 1)
                ship.getGameObject().getTransform().setPosition(new Vector3(pos.x - ((ship.size * 64 - 64) / 2f) - placeOffset.x, pos.y - ((ship.size * 64) / 2f - 32) + placeOffset.y, pos.z));
            else
                ship.getGameObject().getTransform().setPosition(new Vector3(pos.x - placeOffset.x, pos.y + placeOffset.y));
        }
    }

    public Ship ship;

    @Override
    protected void mouseEnter() {
        overlaySprite.color = new Color(255, 255, 255, 150);
        if (game.selectedShip != null) {
            this.ship = game.selectedShip;
        }
    }

    @Override
    protected void mouseExit() {
        overlaySprite.color = new Color(255, 255, 255, 0);
        this.ship = null;
    }

    private ArrayList<FieldElement> getNearby(int x, int y) {
        ArrayList<FieldElement> result = new ArrayList<>();
        result.add(this);
        if (x == 0 && y == 0) {
            result.add(game.thisField[x + 1][y]);
            result.add(game.thisField[x][y + 1]);
            result.add(game.thisField[x + 1][y + 1]);
        }
        else if (x == 9 && y == 0) {
            result.add(game.thisField[x - 1][y]);
            result.add(game.thisField[x][y + 1]);
            result.add(game.thisField[x - 1][y + 1]);
        }
        else if (x == 0 && y == 9) {
            result.add(game.thisField[x + 1][y]);
            result.add(game.thisField[x][y - 1]);
            result.add(game.thisField[x + 1][y - 1]);
        }
        else if (x == 9 && y == 9) {
            result.add(game.thisField[x - 1][y]);
            result.add(game.thisField[x][y - 1]);
            result.add(game.thisField[x - 1][y - 1]);
        }
        else {
            if (x == 0) {
                result.add(game.thisField[x + 1][y]);
                result.add(game.thisField[x + 1][y + 1]);
                result.add(game.thisField[x + 1][y - 1]);
                result.add(game.thisField[x][y + 1]);
                result.add(game.thisField[x][y - 1]);
            }
            else if (x == 9) {
                result.add(game.thisField[x - 1][y]);
                result.add(game.thisField[x - 1][y + 1]);
                result.add(game.thisField[x - 1][y - 1]);
                result.add(game.thisField[x][y + 1]);
                result.add(game.thisField[x][y - 1]);
            }
            else if (y == 0) {
                result.add(game.thisField[x][y + 1]);
                result.add(game.thisField[x + 1][y + 1]);
                result.add(game.thisField[x - 1][y + 1]);
                result.add(game.thisField[x + 1][y]);
                result.add(game.thisField[x - 1][y]);
            }
            else if (y == 9) {
                result.add(game.thisField[x][y - 1]);
                result.add(game.thisField[x + 1][y - 1]);
                result.add(game.thisField[x - 1][y - 1]);
                result.add(game.thisField[x + 1][y]);
                result.add(game.thisField[x - 1][y]);
            }
            else {
                result.add(game.thisField[x + 1][y]);
                result.add(game.thisField[x - 1][y]);
                result.add(game.thisField[x + 1][y + 1]);
                result.add(game.thisField[x - 1][y + 1]);
                result.add(game.thisField[x + 1][y - 1]);
                result.add(game.thisField[x - 1][y - 1]);
                result.add(game.thisField[x][y + 1]);
                result.add(game.thisField[x][y - 1]);
            }
        }

        return result;
    }

    @Override
    protected void mouseUp(int button) {
        if (state == 0 && button == 1 && !isCurrent && game.turn && !game.isGameEnd) {
            state = 1;
            //Send server about shot
            game.turn = false;
            Client.current.sendMessage("shot;" + Client.current.loggedAs + ";" + x + ";" + y);
        }
        else if (state == 0 && button == 1 && isCurrent && !game.isAllShipsReady && ship != null) {
            boolean canSetup = true;
            int down = 0;
            int right = 0;

            if (ship.rot == 0) {
                right = ship.size;
            }
            else {
                down = ship.size;
            }

            for (int x_ = 0; x_ < right; x_++) {
                if (x + x_ - ship.xOffset >= 10)
                    continue;

                ArrayList<FieldElement> nearby = getNearby(x + x_ - ship.xOffset, y - ship.yOffset);
                for (int i = 0; i < nearby.size(); i++) {
                    FieldElement el = nearby.get(i);

                    if (el.state != 0) {
                        canSetup = false;
                    }
                }
            }
            for (int y_ = 0; y_ < down; y_++) {
                if (y + y_ - ship.yOffset >= 10)
                    continue;

                ArrayList<FieldElement> nearby = getNearby(x - ship.xOffset, y + y_ - ship.yOffset);
                for (int i = 0; i < nearby.size(); i++) {
                    FieldElement el = nearby.get(i);

                    if (el.state != 0) {
                        canSetup = false;
                    }
                }
            }

            if (canSetup) {
                ship.x = x;
                ship.y = y;

                Vector3 pos = getGameObject().getTransform().getPosition();
                Vector3 placeOffset = new Vector3();

                for (int i = 0; i < ship.x; i++)
                    placeOffset.x += 64;
                for (int i = 0; i < ship.y; i++)
                    placeOffset.y += 64;

                if (ship.rot == 1)
                    ship.getGameObject().getTransform().setPosition(new Vector3(pos.x - ((ship.size * 64 - 64) / 2f) - placeOffset.x, pos.y - ((ship.size * 64) / 2f - 32) + placeOffset.y, pos.z));
                else
                    ship.getGameObject().getTransform().setPosition(new Vector3(pos.x - placeOffset.x, pos.y + placeOffset.y));

                if (ship.rot == 0) {
                    for (int x = ship.x - ship.xOffset; x < ship.x - ship.xOffset + ship.size; x++) {
                        if (x < 10)
                            game.thisField[x][y].state = 3;
                        else
                            return;
                    }
                }
                if (ship.rot == 1) {
                    for (int y = ship.y - ship.yOffset; y < ship.y - ship.yOffset + ship.size; y++) {
                        if (y < 10)
                            game.thisField[x][y].state = 3;
                        else
                            return;
                    }
                }

                ship.isSetup = true;
                GameController.current.selectedShip = null;

                String fieldS = "";
                for (int y = 0; y < 10; y++) {
                    for (int x = 0; x < 10; x++) {
                        fieldS += GameController.current.thisField[x][y].state;
                    }
                    fieldS += "\n";
                }
                System.out.println(fieldS);
            }
        }
    }
}
