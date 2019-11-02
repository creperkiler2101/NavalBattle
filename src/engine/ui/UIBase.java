package engine.ui;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import engine.base.*;
import engine.core.Application;
import engine.core.Input;
import engine.core.font.*;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class UIBase {
    public Texture sprite;

    private boolean hover = false;
    private boolean pressed = false;
    private boolean selected = false;

    public boolean isHovered() {
        return hover;
    }
    public boolean isPressed() { return pressed; }
    public boolean isSelected() { return selected; }

    private String text = "";
    public String getText() {
        return text;
    }
    public void setText(String text) {
        if (text == null)
            text = "";

        if (text.length() > maxTextLength)
            return;

        this.text = text;
    }

    private Vector3 textOffset = new Vector3();
    public Vector3 getTextOffset() {
        return textOffset;
    }
    public void setTextOffset(Vector3 offset) {
        if (offset == null)
            offset = new Vector3();
        textOffset = offset;
    }

    public Align alignType = Align.CENTER;
    public float top, bottom, left, right;
    public Color color = new Color(255,255,255,255);

    public Font font;
    public float fontSpacing = 5;
    public Vector3 fontScale = new Vector3(1,1,1);
    public Color fontColor = new Color(0, 0, 0);

    public boolean isPassword = false;
    public int maxTextLength = 256;

    private Transform transform;
    public Transform getTransform() {
        return transform;
    }

    public Vector3 getPosition() {
        if (sprite == null) {
            return transform.getPosition();
        }

        Vector3 position = transform.getPosition();
        Vector3 scale = transform.getScale();
        float winWidth = 1920;
        float winHeight = 1080;
        float sWidth = sprite.getImageWidth();
        float sHeight = sprite.getImageHeight();

        switch(alignType) {
            case CENTER:
                position.x += winWidth / 2;
                position.y += winHeight / 2;
                position.x += left - sWidth * scale.x / 2;
                position.y += bottom - sHeight * scale.y / 2;
                break;
            case LEFT:
                position.y += winHeight / 2 - sHeight * scale.y / 2;
                position.x += left;
                position.y += bottom;
                break;
            case LEFT_BOTTOM:
                position.x += 0;
                position.y += 0;
                position.x += left;
                position.y += bottom;
                break;
            case LEFT_TOP:
                position.x += 0;
                position.y += winHeight;
                position.x += left;
                position.y -= top + sHeight * scale.y;
                break;
            case RIGHT:
                position.y += winHeight / 2 - sHeight * scale.y / 2;
                position.x += winWidth - right - sWidth * scale.x;
                position.y += bottom;
                break;
            case RIGHT_BOTTOM:
                position.x += winWidth - sWidth * scale.x;
                position.y += 0;
                position.x -= right;
                position.y += bottom;
                break;
            case RIGHT_TOP:
                position.x += winWidth - sWidth * scale.x;
                position.y += winHeight;
                position.x -= right;
                position.y -= top + sHeight * scale.y;
                break;
            case TOP:
                position.x += winWidth / 2;
                position.y += winHeight;
                position.x += left - sWidth * scale.x / 2;
                position.y -= top + sHeight * scale.y;
                break;
            case BOTTOM:
                position.x += winWidth / 2;
                position.y += 0;
                position.x += left - sWidth * scale.x / 2;
                position.y -= bottom;
                break;
        }

        position.x += Camera.getActiveCamera().getTransform().getPosition().x * 5;
        position.y += Camera.getActiveCamera().getTransform().getPosition().y * 5;

        return position;
    }

    public Bounds getBounds() {
        if (sprite == null)
            return null;

        Bounds result = new Bounds();
        Vector3 pos = getPosition();

        result.bottom = pos.y;
        result.left = pos.x;
        result.top = pos.y + sprite.getImageHeight() * getTransform().getScale().y;
        result.right = pos.x + sprite.getImageWidth() * getTransform().getScale().x;

        //System.out.println(pos.x + " " + pos.y);

        return result;
    }

    public UIBase() {
        transform = new Transform();
    }

    protected void renderText(Vector3 pos) {
        if (font == null)
            return;
        if (fontColor == null)
            fontColor = new Color(0, 0, 0);

        Vector3 fullPosition = new Vector3(textOffset.x + pos.x, textOffset.y + pos.y);

        String toDraw = text;
        if (isPassword)
            toDraw = "*".repeat(text.length());

        font.drawString(toDraw, fullPosition, fontScale, fontColor, fontSpacing);
    }

    private Vector3 render() {
        if (sprite == null)
            return new Vector3(left, bottom);

        Vector3 position = getPosition();
        position.x -= Camera.getActiveCamera().getTransform().getPosition().x * 5;
        position.y -= Camera.getActiveCamera().getTransform().getPosition().y * 5;

        GL2 gl2 = Application.getCurrent().getGL2();

        sprite.enable(gl2);
        sprite.bind(gl2);

        TextureCoords texCoords = sprite.getImageTexCoords();

        gl2.glMatrixMode(GL2.GL_TEXTURE);
        gl2.glLoadIdentity();
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glPushMatrix();

        gl2.glTranslatef(position.x, position.y, transform.getPosition().z);
        gl2.glScalef(transform.getScale().x, transform.getScale().y, transform.getScale().z);

        gl2.glTranslatef(sprite.getImageWidth() / 2f, sprite.getImageHeight() / 2f, 0.0f);
        gl2.glRotatef(transform.getRotation().x,transform.getRotation().y,transform.getRotation().z,1.0f);
        gl2.glTranslatef(-(sprite.getImageWidth() / 2f), -(sprite.getImageHeight() / 2f), 0.0f);

        gl2.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        gl2.glBegin(GL2.GL_QUADS);

        gl2.glVertex2f(0, 0);
        gl2.glTexCoord2f(texCoords.right(), texCoords.bottom());

        gl2.glVertex2f(sprite.getImageWidth(), 0);
        gl2.glTexCoord2f(texCoords.right(), texCoords.top());

        gl2.glVertex2f(sprite.getImageWidth(), sprite.getImageHeight());
        gl2.glTexCoord2f(texCoords.left(), texCoords.top());

        gl2.glVertex2f(0, sprite.getImageHeight());
        gl2.glTexCoord2f(texCoords.left(), texCoords.bottom());

        gl2.glEnd();

        gl2.glPopMatrix();
        //gl2.glFlush();

        sprite.disable(gl2);

        return position;
    }

    public void start() { }

    public void update() {
        if (!hover && Input.isButtonDown(MouseEvent.BUTTON1))
            selected = false;
        renderText(render());
    }

    public void mouseEnter() {
        hover = true;
    }

    public void mouseMove() {

    }

    public void mouseExit() {
        hover = false;
        pressed = false;
    }

    public void mouseDown(int button) {
        if (button == MouseEvent.BUTTON1) {
            selected = true;
            pressed = true;
        }
    }

    public void mousePress(int button) { }

    public void mouseUp(int button) {
        if (button == MouseEvent.BUTTON1)
            pressed = false;
    }

    public void keyPress(KeyEvent event) { }
    public void keyUp(KeyEvent event) { }
}
