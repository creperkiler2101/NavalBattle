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

    private Transform transform;
    public Transform getTransform() {
        return transform;
    }

    public Vector3 getPosition() {
        if (sprite == null) {
            return transform.getPosition();
        }

        Vector3 position = transform.getPosition();
        float winWidth = 1920;
        float winHeight = 1080;
        float sWidth = sprite.getImageWidth();
        float sHeight = sprite.getImageHeight();

        switch(alignType) {
            case CENTER:
                position.x = winWidth / 2;
                position.y = winHeight / 2;
                position.x += left - sWidth / 2;
                position.y += bottom - sHeight / 2;
                break;
            case LEFT:
                position.y = winHeight / 2 - sHeight / 2;
                position.x = left;
                position.y += bottom;
                break;
            case LEFT_BOTTOM:
                position.x = 0;
                position.y = 0;
                position.x += left;
                position.y += bottom;
                break;
            case LEFT_TOP:
                position.x = 0;
                position.y = winHeight;
                position.x += left;
                position.y -= top + sHeight;
                break;
            case RIGHT:
                position.y = winHeight / 2 - sHeight / 2;
                position.x = winWidth - right - sWidth;
                position.y += bottom;
                break;
            case RIGHT_BOTTOM:
                position.x = winWidth - sWidth;
                position.y = 0;
                position.x -= right;
                position.y += bottom;
                break;
            case RIGHT_TOP:
                position.x = winWidth - sWidth;
                position.y = winHeight;
                position.x -= right;
                position.y -= top + sHeight;
                break;
            case TOP:
                position.x = winWidth / 2;
                position.y = winHeight;
                position.x += left - sWidth / 2;
                position.y -= top + sHeight;
                break;
            case BOTTOM:
                position.x = winWidth / 2;
                position.y = 0;
                position.x += left - sWidth / 2;
                position.y -= bottom;
                break;
        }

        return position;
    }

    public Bounds getBounds() {
        if (sprite == null)
            return null;

        Bounds result = new Bounds();
        Vector3 pos = getPosition();

        result.bottom = pos.y;
        result.left = pos.x;
        result.top = (pos.y + sprite.getImageHeight()) * getTransform().getScale().y;
        result.right = (pos.x + sprite.getImageWidth()) * getTransform().getScale().x;

        return result;
    }

    public UIBase() {
        transform = new Transform();
    }

    private void renderText(Vector3 pos) {
        if (font == null)
            return;
        if (fontColor == null)
            fontColor = new Color(0, 0, 0);

        font.drawString(text, pos, fontScale, fontColor, fontSpacing);
    }

    private Vector3 render() {
        if (sprite == null)
            return new Vector3(left, bottom);

        Vector3 position = new Vector3();
        float winWidth = 1920;
        float winHeight = 1080;
        float sWidth = sprite.getImageWidth();
        float sHeight = sprite.getImageHeight();

        switch(alignType) {
            case CENTER:
                position.x = winWidth / 2;
                position.y = winHeight / 2;
                position.x += left - sWidth / 2;
                position.y += bottom - sHeight / 2;
                break;
            case LEFT:
                position.y = winHeight / 2 - sHeight / 2;
                position.x = left;
                position.y += bottom;
                break;
            case LEFT_BOTTOM:
                position.x = 0;
                position.y = 0;
                position.x += left;
                position.y += bottom;
                break;
            case LEFT_TOP:
                position.x = 0;
                position.y = winHeight;
                position.x += left;
                position.y -= top + sHeight;
                break;
            case RIGHT:
                position.y = winHeight / 2 - sHeight / 2;
                position.x = winWidth - right - sWidth;
                position.y += bottom;
                break;
            case RIGHT_BOTTOM:
                position.x = winWidth - sWidth;
                position.y = 0;
                position.x -= right;
                position.y += bottom;
                break;
            case RIGHT_TOP:
                position.x = winWidth - sWidth;
                position.y = winHeight;
                position.x -= right;
                position.y -= top + sHeight;
                break;
            case TOP:
                position.x = winWidth / 2;
                position.y = winHeight;
                position.x += left - sWidth / 2;
                position.y -= top + sHeight;
                break;
            case BOTTOM:
                position.x = winWidth / 2;
                position.y = 0;
                position.x += left - sWidth / 2;
                position.y -= bottom;
                break;
        }

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

        gl2.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

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
    }

    public void mouseDown(int button) {
        System.out.println(button);
        if (button == MouseEvent.BUTTON1) {
            selected = true;
            pressed = true;

            System.out.println(selected + " " + pressed);
        }
    }

    public void mousePress(int button) { }

    public void mouseUp(int button) {
        if (button == MouseEvent.BUTTON1)
            pressed = true;
    }

    public void keyPress(KeyEvent event) { System.out.println(event.getKeyChar());}
    public void keyUp(KeyEvent event) { }
}
