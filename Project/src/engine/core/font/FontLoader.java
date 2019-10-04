package engine.core.font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FontLoader {
    protected static Map<String, Font> fonts = new HashMap<String, Font>();

    public static void LoadFont(String file, String fontName) {
        Font font = new Font(file);
        fonts.put(fontName, font);
    }

    public static Font getFont(String fontName) {
        return fonts.get(fontName);
    }
}
