package sample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCompare {
    public boolean compareWithBaseImage(File baseImage, File compareImage)  throws IOException {
        BufferedImage bImage = ImageIO.read(baseImage);
        BufferedImage cImage = ImageIO.read(compareImage);

        int height = bImage.getHeight();
        int width = bImage.getWidth();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                try {
                    int pixelC = cImage.getRGB(x, y);
                    int pixelB = bImage.getRGB(x, y);
                    if (pixelB != pixelC ) {
                        return false;
                    }
                } catch (Exception e) {
                    // handled height or width mismatch
                    System.out.println(e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
}
