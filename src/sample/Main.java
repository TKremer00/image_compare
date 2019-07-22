package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        File file = new File("F:\\Het huis Anubis ~ Seizoen 1 ~ Aflevering 1+2 22821.jpg");
        File file2 = new File("F:\\Het huis Anubis ~ Seizoen 1 ~ Aflevering 1+2 22821 - kopie.jpg");

        System.out.println(compareWithBaseImage(file,file2));
    }

    public static boolean compareWithBaseImage(File baseImage, File compareImage)  throws IOException {
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


    public static void main(String[] args) {
        launch(args);
    }
}
