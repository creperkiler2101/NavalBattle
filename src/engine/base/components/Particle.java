package engine.base.components;

import engine.base.Component;
import engine.base.Vector3;
import engine.core.Time;

public class Particle extends Component {
    public Vector3 startSpeed;
    public Vector3 endSpeed;
    protected Vector3 currentSpeed;

    public float lifetime;
    protected float lifetimeTimer = 0;

    boolean enabled = false;

    public void enable() {
        delayedExecute(this::kill, lifetime);
        enabled = true;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (startSpeed == null)
            return;

        if (currentSpeed == null) {
            currentSpeed = new Vector3(startSpeed.x, startSpeed.y, startSpeed.z);
        }

        if (endSpeed.x < currentSpeed.x) {
            currentSpeed.x -= (startSpeed.x - endSpeed.x) / lifetime * Time.getDeltaTime();
        }
        else if (endSpeed.x > currentSpeed.x) {
            currentSpeed.x += (startSpeed.x - endSpeed.x) / lifetime * Time.getDeltaTime();
        }

        if (endSpeed.y < currentSpeed.y) {
            currentSpeed.y -= (startSpeed.y - endSpeed.y) / lifetime * Time.getDeltaTime();
        }
        else if (endSpeed.y > currentSpeed.y) {
            currentSpeed.y += (startSpeed.y - endSpeed.y) / lifetime * Time.getDeltaTime();
        }

        getGameObject().getTransform().getLocalPosition().x += currentSpeed.x * Time.getDeltaTime();
        getGameObject().getTransform().getLocalPosition().y += currentSpeed.y * Time.getDeltaTime();

        lifetimeTimer += 1 * Time.getDeltaTime();
        if (lifetimeTimer > lifetime)
            lifetimeTimer = lifetime;
    }

    public void kill() {
        destroy(getGameObject());
    }
}
