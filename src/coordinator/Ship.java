package coordinator;

import engine.base.Vector3;
import game.objects.FieldElement;

import java.util.ArrayList;

public class Ship {
    public int x, y, rotation, size;
    public String sprite;

    public boolean isDestroyed(int[][] field) {
        boolean isAllIsHit = true;

        if (rotation == 0) {
            for (int i = 0; i < size; i++) {
                if (field[x + i][y] != 6)
                    isAllIsHit = false;
            }
        }
        else if (rotation == 1) {
            for (int i = 0; i < size; i++) {
                if (field[x][y + i] != 6)
                    isAllIsHit = false;
            }
        }

        return isAllIsHit;
    }

    public boolean isDestroyed(FieldElement[][] field) {
        boolean isAllIsHit = true;

        if (rotation == 0) {
            for (int i = 0; i < size; i++) {
                if (field[x + i][y].state != 2)
                    isAllIsHit = false;
            }
        }
        else if (rotation == 1) {
            for (int i = 0; i < size; i++) {
                if (field[x][y + i].state != 2)
                    isAllIsHit = false;
            }
        }

        return isAllIsHit;
    }

    public ArrayList<Vector3> getNearby() {
        ArrayList<Vector3> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ArrayList<Vector3> nearby = null;
            if (rotation == 0) {
                nearby = getNearby(x + i, y);
            }
            else if (rotation == 1) {
                nearby = getNearby(x, y + i);
            }

            for (int j = 0; j < nearby.size(); j++)
                result.add(nearby.get(j));
        }

        for (int i = 0; i < result.size(); i++) {
            Vector3 elem = result.get(i);
            for (int j = 0; j < size; j++) {
                if (rotation == 0) {
                    int x = this.x + j;
                    if (elem.x == x && elem.y == y) {
                        result.remove(elem);
                        i--;
                        break;
                    }
                }
                else if (rotation == 1) {
                    int y = this.y + j;
                    if (elem.x == x && elem.y == y) {
                        result.remove(elem);
                        i--;
                        break;
                    }
                }
            }
        }

        return result;
    }

    private ArrayList<Vector3> getNearby(int x, int y) {
        ArrayList<Vector3> result = new ArrayList<>();

        if (y == 0 && x == 0) {
            result.add(new Vector3(x + 1, y));
            result.add(new Vector3(x + 1, y + 1));
            result.add(new Vector3(x, y + 1));
        }
        else if (y == 0 && x == 9) {
            result.add(new Vector3(x - 1, y));
            result.add(new Vector3(x - 1, y + 1));
            result.add(new Vector3(x, y + 1));
        }
        else if (y == 9 && x == 0) {
            result.add(new Vector3(x + 1, y));
            result.add(new Vector3(x + 1, y - 1));
            result.add(new Vector3(x, y - 1));
        }
        else if (y == 9 && x == 9) {
            result.add(new Vector3(x - 1, y));
            result.add(new Vector3(x - 1, y - 1));
            result.add(new Vector3(x, y - 1));
        }
        else {
            if (x == 0) {
                result.add(new Vector3(x + 1, y));
                result.add(new Vector3(x + 1, y + 1));
                result.add(new Vector3(x, y + 1));
                result.add(new Vector3(x + 1, y - 1));
                result.add(new Vector3(x, y - 1));
            }
            else if (x == 9) {
                result.add(new Vector3(x - 1, y));
                result.add(new Vector3(x - 1, y + 1));
                result.add(new Vector3(x, y + 1));
                result.add(new Vector3(x - 1, y - 1));
                result.add(new Vector3(x, y - 1));
            }
            else if (y == 0) {
                result.add(new Vector3(x + 1, y));
                result.add(new Vector3(x - 1, y));
                result.add(new Vector3(x + 1, y + 1));
                result.add(new Vector3(x - 1, y + 1));
                result.add(new Vector3(x, y + 1));
            }
            else if (y == 9) {
                result.add(new Vector3(x + 1, y));
                result.add(new Vector3(x - 1, y));
                result.add(new Vector3(x + 1, y - 1));
                result.add(new Vector3(x - 1, y - 1));
                result.add(new Vector3(x, y - 1));
            }
            else {
                result.add(new Vector3(x - 1, y));
                result.add(new Vector3(x - 1, y + 1));
                result.add(new Vector3(x - 1, y - 1));
                result.add(new Vector3(x + 1, y));
                result.add(new Vector3(x + 1, y + 1));
                result.add(new Vector3(x + 1, y - 1));
                result.add(new Vector3(x, y + 1));
                result.add(new Vector3(x, y - 1));
            }
        }

        return result;
    }
}
