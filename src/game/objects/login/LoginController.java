package game.objects.login;

import engine.base.Camera;
import engine.base.Component;
import engine.base.GameObject;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Input;
import engine.core.Resources;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Button;
import engine.ui.Entry;
import engine.ui.Label;
import game.objects.ui.MyButton;
import game.scenes.RegistrationScene;

import java.awt.*;

public class LoginController extends Component {
    private Label loginPanel;

    private Entry loginEntry, passwordEntry;
    private MyButton loginButton, registrationButton, exitButton;

    @Override
    protected void start() {
        loginPanel = new Label();
        loginPanel.sprite = Resources.getSprite("panel1");
        loginPanel.alignType = Align.CENTER;
        loginPanel.getTransform().setScale(new Vector3(1.5f, 1.5f));
        loginPanel.setText("Welcome!");
        loginPanel.fontScale = new Vector3(0.63f, 0.63f);
        loginPanel.font = FontLoader.getFont("default");
        loginPanel.setTextOffset(new Vector3(200, loginPanel.sprite.getImageHeight() * 1.5f - 95));
        //loginPanel.fontColor = new Color(18, 180, 230);
        loginPanel.bottom = 80;

        loginEntry = new Entry();
        loginEntry.sprite = Resources.getSprite("entry");
        loginEntry.font = FontLoader.getFont("default");
        loginEntry.alignType = Align.CENTER;
        loginEntry.getTransform().setScale(new Vector3(1.6f,1.6f,1.6f));
        loginEntry.fontScale = new Vector3(0.6f, 0.6f);
        loginEntry.setTextOffset(new Vector3(45, 32));
        loginEntry.bottom = 250;
        loginEntry.placeholder = "nickname";
        loginEntry.maxTextLength = 15;

        passwordEntry = new Entry();
        passwordEntry.sprite = Resources.getSprite("entry");
        passwordEntry.font = FontLoader.getFont("default");
        passwordEntry.alignType = Align.CENTER;
        passwordEntry.getTransform().setScale(new Vector3(1.6f,1.6f,1.6f));
        passwordEntry.fontScale = new Vector3(0.6f, 0.6f);
        passwordEntry.setTextOffset(new Vector3(45, 32));
        passwordEntry.bottom = 120;
        passwordEntry.placeholder = "password";
        passwordEntry.maxTextLength = 12;
        passwordEntry.isPassword = true;

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
        loginButton.bottom -= 50;
        loginButton.getTransform().setScale(new Vector3(2, 2));
        loginButton.getTransform().getLocalPosition().z++;
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
        registrationButton.bottom -= 180;
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
        exitButton.bottom -= 360;
        exitButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
        exitButton.setText("exit");
        exitButton.font = FontLoader.getFont("default");
        exitButton.setTextOffset(new Vector3(90, 22));
        exitButton.fontScale = new Vector3(0.8f, 0.8f);

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));

        addGUI(loginPanel);
        addGUI(exitButton);
        addGUI(registrationButton);
        addGUI(loginButton);
        addGUI(loginEntry);
        addGUI(passwordEntry);
    }

    @Override
    protected void update() {

    }

    public void login() {
        String nickname = loginEntry.getText();
        String password = passwordEntry.getText();


    }

    public void showRegistration() {
        Application.getCurrent().setScene(RegistrationScene.class);
    }

    public void exit() {
        System.exit(0);
    }
}
