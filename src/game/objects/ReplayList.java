package game.objects;

import engine.base.Component;
import engine.base.Vector3;
import engine.core.Application;
import engine.core.Resources;
import engine.core.font.FontLoader;
import engine.ui.Align;
import engine.ui.Label;
import game.connection.Client;
import game.objects.ui.MyButton;
import game.scenes.ReplayScene;

import java.util.ArrayList;

public class ReplayList extends Component {
    public ArrayList<game.database.models.Game> games;

    @Override
    protected void start() {

    }

    @Override
    protected void update() {
        if (games == null)
            return;
    }

    public void drawGUI() {
        int distance = 50;
        int size = 200;

        for (int i = 0; i < games.size(); i++) {
            game.database.models.Game game = games.get(i);

            Label panel = new Label();
            panel.alignType = Align.LEFT_TOP;
            panel.getTransform().setScale(new Vector3(1.4f, 1.5f));
            panel.font = FontLoader.getFont("default");
            panel.sprite = Resources.getSprite("EXPpanel");
            panel.setTextOffset(new Vector3(50, 145));
            panel.fontScale = new Vector3(0.5f, 0.5f);
            panel.left = 100;
            panel.top = 40 + size * i + distance * i;
            panel.setText(game.getPlayerOne() + " vs " + game.getPlayerTwo());
            panel.getTransform().parent = getGameObject();

            Label timeText = new Label();
            timeText.alignType = Align.TOP;
            timeText.getTransform().setScale(new Vector3(1, 1));
            timeText.font = FontLoader.getFont("default");
            timeText.sprite = Resources.getSprite("loginPanel");
            timeText.setTextOffset(new Vector3(100, 100));
            timeText.fontScale = new Vector3(0.7f, 0.7f);
            timeText.top = 40 + size * i + distance * i;
            timeText.left = 125;
            timeText.setText(toTimeString(game.getGameLength()));
            timeText.getTransform().parent = getGameObject();

            MyButton seeButton = new MyButton() {
                @Override
                public void mouseUp(int button) {
                    if (button == 1)
                    {
                        ReplayGame g = new ReplayGame(game);
                        Application.getCurrent().setScene(ReplayScene.class);
                    }
                }
            };
            seeButton.alignType = Align.LEFT_TOP;
            seeButton.top = 40 + size * i + distance * i + 100;
            seeButton.left = 200;
            seeButton.getTransform().setScale(new Vector3(1.5f, 1.5f));
            seeButton.setText("play");
            seeButton.font = FontLoader.getFont("default");
            seeButton.setTextOffset(new Vector3(93, 26));
            seeButton.fontScale = new Vector3(0.6f, 0.6f);
            seeButton.getTransform().parent = getGameObject();

            addGUI(panel);
            addGUI(seeButton);
            addGUI(timeText);
        }
    }

    private String toTimeString(int seconds) {
        String minutes_str = "";
        String seconds_str = "";

        int minutes = (int)Math.floor(seconds / 60f);
        if (minutes < 10)
            minutes_str += "0";
        minutes_str += minutes;

        seconds -= minutes * 60;
        if (seconds < 10)
            seconds_str += "0";
        seconds_str += seconds;

        return minutes_str + ":" + seconds_str;
    }
}
