package game.objects.controllers;

import engine.base.Component;
import engine.base.Vector3;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Resources;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Entry;
import engine.ui.Label;
import game.connection.Client;
import game.database.Database;
import game.objects.ui.MyButton;
import game.scenes.MainScene;
import game.scenes.RegistrationScene;

import java.awt.*;
import java.net.InetAddress;

public class LoginController extends Component {
    private static final String ip = "82.131.30.160"; //85.253.128.24
    private static final int port = 25566;

    private Label loginPanel, messagePanel;

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
        loginButton.getTransform().getLocalPosition().z += 0.1f;
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

        messagePanel = new Label();
        messagePanel.sprite = Resources.getSprite("loginPanel");
        messagePanel.alignType = Align.CENTER;
        messagePanel.getTransform().setScale(new Vector3(1f, 1f));
        messagePanel.font = FontLoader.getFont("default");
        messagePanel.fontColor = new Color(0, 0, 0, 0);
        messagePanel.color = new Color(0,0,0,0);
        messagePanel.getTransform().setScale(new Vector3(1.5f, 1.5f));
        messagePanel.getTransform().getLocalPosition().z = 0.5f;

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));

        addGUI(loginPanel);
        addGUI(exitButton);
        addGUI(registrationButton);
        addGUI(loginButton);
        addGUI(loginEntry);
        addGUI(passwordEntry);
        addGUI(messagePanel);
    }

    @Override
    protected void update() {

    }

    public void showMessage(String text, Vector3 textOffset, Vector3 fontScale, boolean dontFade) {
        messagePanel.setText(text);
        messagePanel.setTextOffset(textOffset);
        messagePanel.fontScale = fontScale;
        messagePanel.fontColor = new Color(0, 0, 0, 255);
        messagePanel.color = new Color(200,200,200,255);
        if (!dontFade)
            delayedExecute(this::fade, 1.5f);
    }

    public void fade() {
        try {
            messagePanel.fontColor = new Color(0, 0, 0, messagePanel.fontColor.getAlpha() - 5);
            messagePanel.color = new Color(200, 200, 200, messagePanel.color.getAlpha() - 5);
            if (messagePanel.fontColor.getAlpha() > 1)
                delayedExecute(this::fade, 0.03f);
        }
        catch (Exception ex) { }
    }

    public void login() {
        String nickname = loginEntry.getText();
        String password = passwordEntry.getText();

        if (!Database.logIn(nickname, password)) {
            showMessage("Error!", new Vector3(200, 160), new Vector3(0.8f, 0.8f), false);
            return;
        }

        try {
            showMessage("Connecting...", new Vector3(130, 160), new Vector3(0.6f, 0.6f), true);
            Client client = new Client(InetAddress.getByName(ip), port) {
                @Override
                public void onNotConnected() {
                    showMessage("Error!", new Vector3(200, 160), new Vector3(0.8f, 0.8f), false);
                }

                @Override
                public void onConnected() {
                    Application.getCurrent().setScene(MainScene.class);
                }
            };
            client.connect(nickname);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void showRegistration() {
        Application.getCurrent().setScene(RegistrationScene.class);
    }

    public void exit() {
        System.exit(0);
    }
}
