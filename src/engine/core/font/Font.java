package engine.core.font;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Font {
    public static final char[] chars = new char[] {
            'a','A','b','B','c','C','d','D','e','E',
            'f','F','g','G','h','H','i','I','j','J',
            'k','K','l','L','m','M','n','N','o','O',
            'p','P','q','Q','r','R','s','S','t','T',
            'u','U','v','V','w','W','x','X','y','Y',
            'z','Z',
            /*'а','А','б','Б','в','В','г','Г','д','Д',
            'е','Е','ё','Ё','ж','З','и','И','й','Й',
            'к','К','л','Л','м','М','н','Н','о','О',
            'п','П','р','Р','с','С','т','Т','у','У',
            'ф','Ф','х','Х','ц','Ц','ч','Ч','ш','Ш',
            'щ','Щ','ъ','Ъ','ы','Ы','ь','Ь','э','Э',
            'ю','Ю','я','Я',*/
            '0','1','2','3','4','5','6','7','8','9',
            ',',':','.','!','?','+','-','*','/','\\',
            '=','#','@','_','%',
    };
    public static final int rows = 10;
    public static final int columns = 10;
    public static final int size = 16;
    public static final int separatorSize = 1;

    public Map<Character, Texture> characters = new HashMap<Character, Texture>();

    private String filePath;

    public Font(String file) {
        filePath = file;
        parseFile();
    }

    private void parseFile() {
        try {
            BufferedImage img = ImageIO.read(new File(filePath));
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                    BufferedImage char_ = img.getSubimage(x + separatorSize * x, y + separatorSize * y, size, size);
                    if (x + y * x < chars.length) {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(char_, "gif", os);
                        InputStream is = new ByteArrayInputStream(os.toByteArray());
                        characters.put(chars[x + y * x], TextureIO.newTexture(is, false, ".png"));
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
