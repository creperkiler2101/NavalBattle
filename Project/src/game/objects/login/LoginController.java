package game.objects.login;

import engine.base.Camera;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Resources;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Label;

public class LoginController extends Component {
    private Label loginPanel;

    @Override
    protected void start() {
        loginPanel = new Label();
        loginPanel.sprite = Resources.getSprite("square");
        loginPanel.alignType = Align.CENTER;
        loginPanel.getTransform().setScale(new Vector3(10, 5));
        loginPanel.setText("Login");
        loginPanel.font = FontLoader.getFont("default");
        loginPanel.setTextOffset(new Vector3(10, 10));
        loginPanel.fontScale = new Vector3(0.5f, 0.5f);

        addGUI(loginPanel);
    }

    @Override
    protected void update() {

    }
}
