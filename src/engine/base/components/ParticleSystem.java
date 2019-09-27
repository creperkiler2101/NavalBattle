package engine.base.components;

import com.jogamp.opengl.util.texture.Texture;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.core.Application;
import engine.core.Time;

import java.util.ArrayList;
import java.util.Random;

public final class ParticleSystem extends Component {
    public ArrayList<Texture> sprites = new ArrayList<Texture>();

    public Vector3 minStartSpeed = new Vector3();
    public Vector3 maxStartSpeed = new Vector3();
    public Vector3 minEndSpeed = new Vector3();
    public Vector3 maxEndSpeed = new Vector3();

    public Vector3 minStartRotation = new Vector3();
    public Vector3 maxStartRotation = new Vector3();
    public Vector3 minEndRotation = new Vector3();
    public Vector3 maxEndRotation = new Vector3();

    public Vector3 minStartScale = new Vector3();
    public Vector3 maxStartScale = new Vector3();
    public Vector3 minEndScale = new Vector3();
    public Vector3 maxEndScale = new Vector3();

    public float minLifetime = 1;
    public float maxLifetime = 5;

    public float spawnDelay = 0;
    protected float spawnDelayTimer = 0;

    @Override
    protected void start() {

    }

    @Override
    protected void update() {
        if (sprites.size() == 0)
            return;

        spawnDelayTimer += 1 * Time.getDeltaTime();
        if (spawnDelayTimer >= spawnDelay) {
            spawnDelayTimer = 0;
            spawnParticle();
        }
    }

    protected void spawnParticle() {
        Random random = new Random();
        int spriteIndex = random.nextInt(sprites.size());

        float startSpeedX = (float)(minStartSpeed.x + Math.random() * (maxStartSpeed.x - minStartSpeed.x));
        float startSpeedY = (float)(minStartSpeed.y + Math.random() * (maxStartSpeed.y - minStartSpeed.y));
        float startSpeedZ = (float)(minStartSpeed.z + Math.random() * (maxStartSpeed.z - minStartSpeed.z));
        Vector3 startSpeed = new Vector3(startSpeedX, startSpeedY, startSpeedZ);

        float endSpeedX = (float)(minEndSpeed.x + Math.random() * (maxEndSpeed.x - minEndSpeed.x));
        float endSpeedY = (float)(minEndSpeed.y + Math.random() * (maxEndSpeed.y - minEndSpeed.y));
        float endSpeedZ = (float)(minEndSpeed.z + Math.random() * (maxEndSpeed.z - minEndSpeed.z));
        Vector3 endSpeed = new Vector3(endSpeedX, endSpeedY, endSpeedZ);

        GameObject particle = new GameObject();
        Particle component = particle.addComponent(Particle.class);
        SpriteRenderer sr = particle.addComponent(SpriteRenderer.class);
        sr.sprite = sprites.get(spriteIndex);
        component.lifetime = 1;
        component.startSpeed = startSpeed;
        component.endSpeed = endSpeed;

        particle.getTransform().parent = getGameObject();
        instantiate(particle);
        component.enable();
    }
}
