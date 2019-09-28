package engine.base.components;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import engine.base.Camera;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Transform;
import engine.core.Application;

import java.awt.*;

public class SpriteRenderer extends Component {
    public Texture sprite = null;
    public Color color = new Color(255, 255, 255, 255);

    @Override
    protected void start() {

    }

    @Override
    protected void update() {
        if (sprite == null)
            return;

        GL2 gl2 = Application.getCurrent().getGL2();

        gl2.glDisable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_TEXTURE_2D);
        gl2.glEnable(GL2.GL_POINT_SMOOTH);
        gl2.glEnable(GL2.GL_COLOR_MATERIAL);

        gl2.glDepthMask(false);
        gl2.glEnable(GL2.GL_BLEND);
        gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        sprite.enable(gl2);
        sprite.bind(gl2);

        Transform transform = getGameObject().getTransform();
        TextureCoords texCoords = sprite.getImageTexCoords();

        gl2.glMatrixMode(GL2.GL_TEXTURE);
        gl2.glLoadIdentity();
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glPushMatrix();

        gl2.glTranslatef(transform.getPosition().x, transform.getPosition().y, transform.getPosition().z);
        gl2.glScalef(transform.getScale().x, transform.getScale().y, transform.getScale().z);

        gl2.glTranslatef(sprite.getImageWidth() / 2f, sprite.getImageHeight() / 2f, 0.0f);
        gl2.glRotatef(transform.getRotation().x,transform.getRotation().y,transform.getRotation().z,1.0f);
        gl2.glTranslatef(-(sprite.getImageWidth() / 2f), -(sprite.getImageHeight() / 2f), 0.0f);

        Transform cameraTransform = Camera.getActiveCamera().getTransform();
        gl2.glTranslatef(-cameraTransform.getPosition().x, -cameraTransform.getPosition().y, cameraTransform.getPosition().z);
        gl2.glScalef(cameraTransform.getScale().x, cameraTransform.getScale().y, cameraTransform.getScale().z);

        gl2.glTranslatef(Application.getCurrent().getWindowWidth() / 2f, Application.getCurrent().getWindowHeight() / 2f, 0.0f);
        gl2.glRotatef(cameraTransform.getRotation().x, cameraTransform.getRotation().y, cameraTransform.getRotation().z, 1.0f);
        gl2.glTranslatef(-(Application.getCurrent().getWindowWidth() / 2f), -(Application.getCurrent().getWindowHeight() / 2f), 0.0f);

        gl2.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        gl2.glBegin(GL2.GL_QUADS);

        gl2.glVertex2f(0, 0);
        gl2.glTexCoord2f(texCoords.right(), texCoords.bottom());

        gl2.glVertex2f(sprite.getImageHeight(), 0);
        gl2.glTexCoord2f(texCoords.right(), texCoords.top());

        gl2.glVertex2f(sprite.getImageHeight(), sprite.getImageWidth());
        gl2.glTexCoord2f(texCoords.left(), texCoords.top());

        gl2.glVertex2f(0, sprite.getImageWidth());
        gl2.glTexCoord2f(texCoords.left(), texCoords.bottom());

        gl2.glEnd();

        gl2.glPopMatrix();
        gl2.glFlush();

        gl2.glDepthMask(true);
        gl2.glDisable(GL2.GL_BLEND);

        sprite.disable(gl2);
    }
}
