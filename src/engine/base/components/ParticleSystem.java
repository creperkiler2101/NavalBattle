package engine.base.components;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;

import java.util.ArrayList;

public final class ParticleSystem extends Component {
    protected ArrayList<GameObject> particles = new ArrayList<GameObject>();
    public ArrayList<Texture> sprites = new ArrayList<Texture>();

    public Vector3 minStartSpeed = new Vector3();
    public Vector3 maxStartSpeed = new Vector3();
    public Vector3 minEndSpeed = new Vector3();
    public Vector3 maxEndSpeed = new Vector3();

    public Vector3 minStartRotation = new Vector3();
    public Vector3 maxStartRotation = new Vector3();
    public Vector3 minEndRotation = new Vector3();
    public Vector3 maxEndRotation = new Vector3();

    public int minLifetime = 1;
    public int maxLifetime = 5;

    public int maxParticlesCount = 100;

    @Override
    protected void start() {

    }

    @Override
    protected void update() {
        if (sprites.size() == 0)
            return;
    }
}
