import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Resources {
    private static Map<String, BufferedImage> images = new HashMap<>();

    static BufferedImage getImage(String name){
        if (!images.containsKey(name)){
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("./resources/" + name + ".png"));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка загрузки ресурсов");
                System.exit(1);
            }
            images.put(name, image);
        }
        return  images.get(name);
    }
}
