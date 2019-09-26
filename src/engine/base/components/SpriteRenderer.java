package engine.base.components;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
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

        sprite.enable(gl2);
        sprite.bind(gl2);

        Transform transform = getGameObject().getTransform();

        float halfW = sprite.getImageWidth() / 2f;
        float halfH = sprite.getImageHeight() / 2f;

        float left = transform.position.x;
        float right = transform.position.x + sprite.getImageWidth();

        float bottom = -transform.position.y;
        float top = -transform.position.y + sprite.getImageHeight();

        //right *= transform.scale.x;
        //top *= transform.scale.y;

        //gl2.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
       // gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        //gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,GL2. GL_CLAMP_TO_EDGE);

        gl2.glMatrixMode(GL2.GL_TEXTURE);
        gl2.glLoadIdentity();
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glPushMatrix();


        TextureCoords texCoords = sprite.getImageTexCoords();

        gl2.glTranslatef(transform.position.x, transform.position.y, transform.position.z);
        gl2.glScalef(transform.scale.x, transform.scale.y, transform.scale.z);
        gl2.glTranslatef(sprite.getImageWidth() / 2f, sprite.getImageHeight() / 2f, 0.0f);
        gl2.glRotatef(transform.rotation.x,transform.rotation.y,transform.rotation.z,1.0f);
        gl2.glTranslatef(-(sprite.getImageWidth() / 2f), -(sprite.getImageHeight() / 2f), 0.0f);

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

        sprite.disable(gl2);
    }
}
