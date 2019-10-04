package engine.core;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Resources {
    private static Map<String, Texture> resources = new HashMap<String, Texture>();

    private static boolean isImage(String expr) {
        return expr.equals("jpg") ||
                expr.equals("jpeg") ||
                expr.equals("png") ||
                expr.equals("gif") ||
                expr.equals("bmp");
    }

    protected static void load(String path) {
        System.out.println("Loading resources");
        resources.clear();
        GL2 gl2 = Application.getCurrent().getGL2();

        File folder = null;
        try {
            URL url = new URL(path);
            folder = new File(url.getFile().substring(1).replace("%20", " "));
        }
        catch (Exception ex) {
            System.out.println("Folder not found");
            return;
        }

        for (final File file : folder.listFiles()) {
            String fileName = file.getName();
            String split[] = fileName.split("[.]");
            String accessName = "";
            for (int i = 0; i < split.length - 1; i++) {
                accessName += split[i];
            }

            String expr = split[split.length - 1];
            try {
                if (isImage(expr)) {
                    TextureData td = TextureIO.newTextureData(gl2.getGLProfile(), new URL("file:///" + file.getPath()), false, "." + expr);
                    td.setPixelFormat(GL2.GL_RGBA);
                    Texture t = TextureIO.newTexture(td);
                    gl2.glTexParameteri(t.getTextureObject(), GL2.GL_IMAGE_PIXEL_FORMAT, GL2.GL_RGBA);
                    t.setTexParameteri(gl2, GL2.GL_IMAGE_PIXEL_FORMAT, GL2.GL_RGBA16);
                    t.setTexParameteri(gl2, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
                    t.setTexParameteri(gl2, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
                    t.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
                    t.setTexParameteri(gl2, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

                    resources.put(accessName, t);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("OK");
    }

    public static Texture getSprite(String name) {
        return resources.get(name);
    }
}
