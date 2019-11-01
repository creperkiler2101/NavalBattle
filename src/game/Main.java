package game;

import engine.base.GameObject;
import engine.base.Scene;
import engine.base.Vector3;
import engine.base.components.ParticleSystem;
import engine.base.components.SpriteRenderer;
import engine.core.Application;
import engine.core.Resources;
import engine.core.font.FontLoader;
import game.connection.Client;
import game.database.Database;
import game.database.models.Player;
import game.scenes.LoginScene;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.xml.crypto.Data;
import java.net.InetAddress;

public class Main {
    public static boolean isRelease = true;

    public static void main(String[] args) {
        Application app = new Application() {
            @Override
            public void onGLInitialized() {
                setScene(LoginScene.class);
            }

            @Override
            public void resourceLoad() {
                if (!isRelease) {
                    this.loadResources(Main.class.getResource("resources").toString(), true);
                    FontLoader.LoadFont(Main.class.getResource("Font.png").toString().replace("%20", " ").replace("/", "\\").substring(6), "default");
                }
                else {
                    this.loadResources(System.getProperty("user.dir") + "\\resources", false);
                    FontLoader.LoadFont(System.getProperty("user.dir") + "\\resources\\font.png", "default");
                }

            }

            @Override
            public void onClose() {
                if (Client.current != null) {
                    if (Client.current.isConnected) {
                        Client.current.sendMessage("disconnect;" + Client.current.loggedAs);
                        Client.current.isConnected = false;
                    }
                }
            }
        };
        app.show();
        app.setTitle("Naval Battle");
        //Application.getCurrent().goFullscreen();
    }
}
