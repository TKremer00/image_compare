package sample;

import java.io.File;

public class FileHandler {

    private String inputFolder;
    private String inputFile1,inputFile2;
    private String fileExtention = ".jpg";

    public FileHandler(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public FileHandler(String inputFile1,String inputFile2) {
        this.inputFile1 = inputFile1;
        this.inputFile2 = inputFile2;
    }


    public void deleteSameImages() {
        File dir = new File(inputFolder);
        File[] files = dir.listFiles((d, name) -> name.endsWith(fileExtention));

        for (int i = 1; i < files.length; i++) {
            if(ImageCompare.compareWithBaseImage(files[i - 1], files[i])) {
                System.out.println((i -1 ) + " -> " +files[i - 1].getName() + " with " + i + " -> " +files[i].getName());
            }
        }
    }


}
