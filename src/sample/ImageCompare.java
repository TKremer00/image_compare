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

        int[][] points = new int[][] {
                {(int) Math.floor(bImage.getWidth() / 2),(int) Math.floor(bImage.getHeight() / 2)}, // center
                {5,5}, // top left
                {bImage.getWidth() - 5,5 }, // top right
                {5, bImage.getHeight() - 5}, // bottom left
                {bImage.getWidth() - 5, bImage.getHeight() - 5} // bottom right
        };

        for (int[] point: points) {
            if (cImage.getRGB(point[0], point[1]) != bImage.getRGB(point[0], point[1]) )
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
