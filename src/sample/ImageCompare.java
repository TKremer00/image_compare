package sample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageCompare {

    public static boolean compareImage(File baseImage, File compareImage) {

        BufferedImage bImage;
        BufferedImage cImage;

        try {
            bImage = ImageIO.read(baseImage);
            cImage = ImageIO.read(compareImage);
        }catch (Exception e) {
            System.out.println("Read Image\n" + e.getMessage());
            return false;
        }

        if(bImage.getHeight() != cImage.getHeight() || bImage.getWidth() != cImage.getWidth()) {
            return false;
        }

        //Check every pixel
        for (int y = 0; y < bImage.getHeight(); y++) {
            for (int x = 0; x < bImage.getWidth(); x++) {
                if (cImage.getRGB(x, y) != bImage.getRGB(x, y) )
                    return false;
            }
        }

        // If all checks out then the images is the same
        return true;
    }
}
