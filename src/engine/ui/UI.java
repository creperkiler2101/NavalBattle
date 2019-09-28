package engine.ui;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import engine.base.Camera;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Input;

import java.awt.*;
import java.awt.event.MouseEvent;

public class UI extends Component {
    private TextRenderer textRenderer;

    public Texture sprite;

    private boolean hover = false;
    public boolean isHovered() {
        return hover;
    }

    private boolean selected = false;

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

    public Font font = new Font("TimesRoman", Font.PLAIN, 24);
    public Color textColor = new Color(0, 0, 0);

    private void renderText() {
        if (font == null)
            font = new Font("TimesRoman", Font.PLAIN, 24);
        if (textColor == null)
            textColor = new Color(0, 0, 0);

        textRenderer = new TextRenderer(font);
        textRenderer.beginRendering((int)Application.getCurrent().getWindowWidth(), (int)Application.getCurrent().getWindowHeight());
        textRenderer.setColor(textColor);

        Vector3 textPosition = getGameObject().getTransform().getPosition();
        textPosition.x += textOffset.x;
        textPosition.y += textOffset.y;
        textRenderer.draw(text, (int)textPosition.x, (int)textPosition.y);
        textRenderer.endRendering();
    }

    private void render() {
        GL2 gl2 = Application.getCurrent().getGL2();
    }

    @Override
    protected void start() { }

    @Override
    protected void update() {
        if (!hover && Input.isButtonDown(MouseEvent.BUTTON1))
            selected = false;
        render();
        renderText();
    }

    @Override
    protected void mouseEnter() {
        hover = true;
    }

    @Override
    protected void mouseMove() {

    }

    @Override
    protected void mouseExit() {
        hover = false;
    }

    @Override
    protected void mouseDown(int button) {
        if (button == MouseEvent.BUTTON1)
            selected = true;
    }
    @Override
    protected void mousePress(int button) { }
    @Override
    protected void mouseUp(int button) { }
}
