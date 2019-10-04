package game.objects.login;

import engine.base.Camera;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Input;
import engine.core.Resources;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Button;
import engine.ui.Entry;
import engine.ui.Label;
import game.objects.ui.MyButton;

import java.awt.*;

public class LoginController extends Component {
    private Label loginPanel;

    private Entry loginEntry, passwordEntry;
    private MyButton loginButton, registrationButton, exitButton;

    @Override
    protected void start() {
        loginPanel = new Label();
        loginPanel.sprite = Resources.getSprite("loginPanel");
        loginPanel.alignType = Align.CENTER;
        loginPanel.getTransform().setScale(new Vector3(2, 2));
        loginPanel.setText("Welcome!");
        loginPanel.fontScale = new Vector3(0.63f, 0.63f);
        loginPanel.font = FontLoader.getFont("default");
        loginPanel.setTextOffset(new Vector3(280, loginPanel.sprite.getImageHeight() * 2 - 88 * 2));
        loginPanel.fontColor = new Color(18, 180, 230);
        loginPanel.bottom = 180;

        loginEntry = new Entry();
        loginEntry.sprite = Resources.getSprite("square");
        loginEntry.font = FontLoader.getFont("default");

        loginButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1) {
                    login();
                }
            }
        };
        loginButton.alignType = Align.CENTER;
        loginButton.bottom -= 70;
        loginButton.getTransform().setScale(new Vector3(2, 2));
        loginButton.setText("log in");
        loginButton.font = FontLoader.getFont("default");
        loginButton.setTextOffset(new Vector3(116, 38));
        loginButton.fontScale = new Vector3(0.8f, 0.8f);

        registrationButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1) {
                    showRegistration();
                }
            }
        };
        registrationButton.alignType = Align.CENTER;
        registrationButton.bottom -= 200;
        registrationButton.getTransform().setScale(new Vector3(2, 2));
        registrationButton.fontScale = new Vector3(0.5f, 0.5f);
        registrationButton.setText("registration");
        registrationButton.font = FontLoader.getFont("default");
        registrationButton.setTextOffset(new Vector3(63, 45));

        exitButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1) {
                    exit();
                }
            }
        };
        exitButton.alignType = Align.CENTER;
        exitButton.bottom -= 330;
        exitButton.getTransform().setScale(new Vector3(2, 2));
        exitButton.setText("exit");
        exitButton.font = FontLoader.getFont("default");
        exitButton.setTextOffset(new Vector3(135, 38));
        exitButton.fontScale = new Vector3(0.8f, 0.8f);

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));

        addGUI(exitButton);
        addGUI(registrationButton);
        addGUI(loginButton);
        addGUI(loginPanel);
    }

    @Override
    protected void update() {

    }

    public void login() {
        System.out.println("login");
    }

    public void showRegistration() {
        System.out.println("registration");
    }

    public void exit() {
        System.exit(0);
    }
}
