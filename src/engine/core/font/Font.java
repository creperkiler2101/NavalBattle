package engine.core.font;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;
import engine.base.Camera;
import engine.base.Transform;
import engine.base.Vector3;
import engine.core.Application;

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
            '=','#','@','_','%',' ','(',')','[',']',
            '{','}'
    };
    public static final int rows = 15;
    public static final int columns = 15;
    public static final int size = 15;
    public static final int separatorSize = 1;

    public Map<Character, Texture> characters = new HashMap<Character, Texture>();

    private String filePath;

    public Font(String file) {
        filePath = file;
        parseFile();
    }

    private void parseFile() {
        try {
            //System.out.println(filePath);
            BufferedImage img = ImageIO.read(new File(filePath));
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                    BufferedImage char_ = img.getSubimage(x + size * x + separatorSize * x, y + size * y + separatorSize * y, size, size);
                    if (x + y * columns < chars.length) {
                        //System.out.println(chars[x + y * columns]);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(char_, "png", os);
                        InputStream is = new ByteArrayInputStream(os.toByteArray());
                        characters.put(chars[x + y * columns], TextureIO.newTexture(is, false, ".png"));
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void drawString(String text, Vector3 position, Vector3 scale, Color color) {
        float xOffset = 0;

        for (char sym : text.toCharArray()) {
            boolean contains = false;
            for (char ch : chars) {
                if (sym == ch) {
                    contains = true;
                    break;
                }
            }
          //  System.out.println(contains);
            if (!contains)
                continue;

            Texture texture = characters.get(sym);
            if (texture == null)
                return;

            GL2 gl2 = Application.getCurrent().getGL2();

            gl2.glDisable(GL2.GL_LIGHTING);
            gl2.glEnable(GL2.GL_TEXTURE_2D);
            gl2.glEnable(GL2.GL_POINT_SMOOTH);
            gl2.glEnable(GL2.GL_COLOR_MATERIAL);

            gl2.glDepthMask(false);
            gl2.glEnable(GL2.GL_BLEND);
            gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

            texture.enable(gl2);
            texture.bind(gl2);

            TextureCoords texCoords = texture.getImageTexCoords();

            gl2.glMatrixMode(GL2.GL_TEXTURE);
            gl2.glLoadIdentity();
            gl2.glMatrixMode(GL2.GL_MODELVIEW);
            gl2.glPushMatrix();

            gl2.glTranslatef(position.x + xOffset, position.y, position.z);
            gl2.glScalef(scale.x, scale.y, scale.z);

            gl2.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

            gl2.glBegin(GL2.GL_QUADS);

            gl2.glVertex2f(0, 0);
            gl2.glTexCoord2f(texCoords.right(), texCoords.bottom());

            gl2.glVertex2f(texture.getImageHeight(), 0);
            gl2.glTexCoord2f(texCoords.right(), texCoords.top());

            gl2.glVertex2f(texture.getImageHeight(), texture.getImageWidth());
            gl2.glTexCoord2f(texCoords.left(), texCoords.top());

            gl2.glVertex2f(0, texture.getImageWidth());
            gl2.glTexCoord2f(texCoords.left(), texCoords.bottom());

            gl2.glEnd();

            gl2.glPopMatrix();
            gl2.glFlush();

            gl2.glDepthMask(true);
            gl2.glDisable(GL2.GL_BLEND);

            texture.disable(gl2);

            xOffset += size * scale.x + 5;
        }
    }
}
