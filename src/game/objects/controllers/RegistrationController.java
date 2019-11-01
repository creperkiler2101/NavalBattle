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
import game.database.Database;
import game.database.models.Player;
import game.objects.ui.MyButton;
import game.scenes.LoginScene;
import org.hibernate.Session;

import java.awt.*;

public class RegistrationController extends Component {
    private Label loginPanel, messagePanel;

    private Entry loginEntry, passwordEntry, rePasswordEntry;
    private MyButton registrationButton, backButton;

    @Override
    protected void start() {
        loginPanel = new Label();
        loginPanel.sprite = Resources.getSprite("panel1");
        loginPanel.alignType = Align.CENTER;
        loginPanel.getTransform().setScale(new Vector3(1.5f, 1.5f));
        loginPanel.setText("Registration");
        loginPanel.fontScale = new Vector3(0.63f, 0.63f);
        loginPanel.font = FontLoader.getFont("default");
        loginPanel.setTextOffset(new Vector3(170, loginPanel.sprite.getImageHeight() * 1.5f - 95));
        loginPanel.bottom = 80;

        messagePanel = new Label();
        messagePanel.sprite = Resources.getSprite("loginPanel");
        messagePanel.alignType = Align.CENTER;
        messagePanel.getTransform().setScale(new Vector3(1f, 1f));
        messagePanel.font = FontLoader.getFont("default");
        messagePanel.fontColor = new Color(0, 0, 0, 0);
        messagePanel.color = new Color(0,0,0,0);
        messagePanel.getTransform().setScale(new Vector3(1.5f, 1.5f));

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

        rePasswordEntry = new Entry();
        rePasswordEntry.sprite = Resources.getSprite("entry");
        rePasswordEntry.font = FontLoader.getFont("default");
        rePasswordEntry.alignType = Align.CENTER;
        rePasswordEntry.getTransform().setScale(new Vector3(1.6f,1.6f,1.6f));
        rePasswordEntry.fontScale = new Vector3(0.6f, 0.6f);
        rePasswordEntry.setTextOffset(new Vector3(45, 32));
        rePasswordEntry.bottom = -10;
        rePasswordEntry.placeholder = "re-password";
        rePasswordEntry.maxTextLength = 12;
        rePasswordEntry.isPassword = true;

        registrationButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1) {
                    register();
                }
            }
        };
        registrationButton.alignType = Align.CENTER;
        registrationButton.bottom -= 180;
        registrationButton.getTransform().setScale(new Vector3(2, 2));
        registrationButton.fontScale = new Vector3(1, 1);
        registrationButton.setText("go");
        registrationButton.font = FontLoader.getFont("default");
        registrationButton.setTextOffset(new Vector3(150, 30));

        backButton = new MyButton() {
            @Override
            public void mouseUp(int button) {
                super.mouseUp(button);
                if (button == 1) {
                    back();
                }
            }
        };
        backButton.alignType = Align.CENTER;
        backButton.bottom -= 360;
        backButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
        backButton.setText("back");
        backButton.font = FontLoader.getFont("default");
        backButton.setTextOffset(new Vector3(62, 22));
        backButton.fontScale = new Vector3(0.8f, 0.8f);

        SpriteRenderer sr = getGameObject().addComponent(SpriteRenderer.class);
        sr.sprite = Resources.getSprite("seaBackground1");
        getGameObject().getTransform().setScale(new Vector3(2.6f,2.6f));

        addGUI(loginPanel);
        addGUI(backButton);
        addGUI(registrationButton);
        addGUI(loginEntry);
        addGUI(passwordEntry);
        addGUI(rePasswordEntry);
        addGUI(messagePanel);
    }

    @Override
    protected void update() {

    }

    public void showMessage(String text, Vector3 textOffset, Vector3 fontScale) {
        messagePanel.setText(text);
        messagePanel.setTextOffset(textOffset);
        messagePanel.fontScale = fontScale;
        messagePanel.fontColor = new Color(0, 0, 0, 255);
        messagePanel.color = new Color(200,200,200,255);
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

    public void register() {
        String nickname = loginEntry.getText();
        String password = passwordEntry.getText();
        String rePassword = rePasswordEntry.getText();

        if (password.isBlank() || nickname.isBlank()) {
            showMessage("Error!", new Vector3(200, 160), new Vector3(0.8f, 0.8f));
            return;
        }

        if (!password.equals(rePassword)) {
            showMessage("Error!", new Vector3(200, 160), new Vector3(0.8f, 0.8f));
            return;
        }

        if (Database.isPlayerExists(nickname)) {
            showMessage("In use!", new Vector3(170, 160), new Vector3(0.8f, 0.8f));
            return;
        }


        Player p = new Player();
        p.setNickname(nickname);
        p.setPassword(password);
        p.setGameCount(0);
        p.setWins(0);
        p.setLoses(0);
        p.setExperience(0);

        Database.insert(p);

        showMessage("Success!", new Vector3(130, 160), new Vector3(0.8f, 0.8f));
    }

    public void back() {
        Application.getCurrent().setScene(LoginScene.class);
    }
}
