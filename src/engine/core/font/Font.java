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
    public static final int size = 16 * 8;
    public static final int separatorSize = 1 * 8;

    public Map<Character, Texture> characters = new HashMap<Character, Texture>();

    private String filePath;

    public Font(String file) {
        filePath = file;
        parseFile();
    }

    private void parseFile() {
        try {

            GL2 gl2 = Application.getCurrent().getGL2();
            //System.out.println(filePath);
            BufferedImage img = ImageIO.read(new File(filePath));
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                    BufferedImage char_ = img.getSubimage(x + (size - 1) * x + separatorSize * x, y + (size - 1) * y + separatorSize * y, size, size);
                    if (x + y * columns < chars.length) {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        int leftOffset = 0;
                        int rightOffset = 0;

                        boolean isEmptyLeft = true;
                        for (int x_ = 0; x_ < size; x_++) {
                            if (isEmptyLeft) {
                                for (int y_ = 0; y_ < size; y_++) {
                                    Color c = new Color(char_.getRGB(x_, y_), true);
                                    //System.out.println(x_ + " " + y_ + " " + c.getAlpha());
                                    if (c.getAlpha() > 0) {
                                        isEmptyLeft = false;
                                        break;
                                    }
                                }
                            }
                            if (isEmptyLeft)
                                leftOffset++;
                        }

                        boolean isEmptyRight = true;
                        for (int x_ = size - 1; x_ >= 0; x_--) {
                            for (int y_ = 0; y_ < size; y_++) {
                                Color c = new Color(char_.getRGB(x_, y_), true);
                                if (c.getAlpha() > 0) {
                                    isEmptyRight = false;
                                    break;
                                }
                            }
                            if (isEmptyRight)
                                rightOffset++;
                        }

                        BufferedImage toLoad = char_.getSubimage(leftOffset, 0, size - rightOffset - leftOffset, size);
                        ImageIO.write(toLoad, "png", os);
                        InputStream is = new ByteArrayInputStream(os.toByteArray());
                        Texture t = TextureIO.newTexture(is, false, ".png");

                        t.setTexParameteri(gl2, GL2.GL_IMAGE_PIXEL_FORMAT, GL2.GL_RGBA16);
                        t.setTexParameteri(gl2, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
                        t.setTexParameteri(gl2, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
                        t.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
                        t.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

                        characters.put(chars[x + y * columns], t);
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void drawString(String text, Vector3 position, Vector3 scale, Color color, float spacing) {
        float xOffset = 0;

        for (char sym : text.toCharArray()) {
            boolean contains = false;
            for (char ch : chars) {
                if (sym == ch) {
                    contains = true;
                    break;
                }
            }

            if (!contains)
                continue;

            Texture texture = characters.get(sym);
            if (texture == null)
                return;

            if (sym == ' ') {
                xOffset += spacing * 4 * scale.x + spacing;
                continue;
            }

            GL2 gl2 = Application.getCurrent().getGL2();

            /*gl2.glDisable(GL2.GL_LIGHTING);
            gl2.glEnable(GL2.GL_TEXTURE_2D);
            gl2.glEnable(GL2.GL_POINT_SMOOTH);
            gl2.glEnable(GL2.GL_COLOR_MATERIAL);

            gl2.glDepthMask(false);
            gl2.glEnable(GL2.GL_BLEND);
            gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
*/
            texture.enable(gl2);
            texture.bind(gl2);

            TextureCoords texCoords = texture.getImageTexCoords();

            gl2.glMatrixMode(GL2.GL_TEXTURE);
            gl2.glLoadIdentity();
            gl2.glMatrixMode(GL2.GL_MODELVIEW);
            gl2.glPushMatrix();

            gl2.glTranslatef(position.x + xOffset, position.y, position.z + 0.1f);
            gl2.glScalef(scale.x, scale.y, scale.z);

            gl2.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

            gl2.glBegin(GL2.GL_QUADS);

            gl2.glVertex2f(0, 0);
            gl2.glTexCoord2f(texCoords.right(), texCoords.bottom());

            gl2.glVertex2f(texture.getImageWidth(), 0);
            gl2.glTexCoord2f(texCoords.right(), texCoords.top());

            gl2.glVertex2f(texture.getImageWidth(), texture.getImageHeight());
            gl2.glTexCoord2f(texCoords.left(), texCoords.top());

            gl2.glVertex2f(0, texture.getImageHeight());
            gl2.glTexCoord2f(texCoords.left(), texCoords.bottom());

            gl2.glEnd();

            gl2.glPopMatrix();
            //gl2.glFlush();

            //gl2.glDepthMask(true);
            //gl2.glDisable(GL2.GL_BLEND);

            texture.disable(gl2);

            xOffset += texture.getImageWidth() * scale.x + spacing;
        }
    }
}
