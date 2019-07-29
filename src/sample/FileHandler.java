package sample;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {

    private String inputDirectory;
    private String inputFile1,inputFile2;
    private String fileExtension;
    private ArrayList<ArrayList<String>> deletedImages = new ArrayList<>();


    public void deleteSameImages() {
        File dir = new File(inputDirectory);
        System.out.println(inputDirectory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(fileExtension));

        for (int i = 1; i < files.length; i++) {
            if(ImageCompare.compareWithBaseImage(files[i - 1], files[i])) {
                deletedImages.add(new ArrayList<>(Arrays.asList(removeExtensions(files[i - 1].getName()), removeExtensions(files[i].getName()))));
                System.out.println((i -1 ) + " -> " +files[i - 1].getName() + " with " + i + " -> " +files[i].getName());
            }
        }

        for (ArrayList<String> data: deletedImages) {
            File file = new File(inputDirectory + data.get(1) + "." + fileExtension);
            if(!file.delete()){
                System.out.println("Error file not deleted " + data.get(1));
            }
        }
        createDataLog();
    }

    public void compareImages() {
        File dir = new File(inputDirectory);
        File[] files = dir.listFiles((d, name) -> name.endsWith("." + fileExtension));

        for (int i = 1; i < files.length; i++) {
            if(ImageCompare.compareWithBaseImage(files[i - 1], files[i])) {
                deletedImages.add(new ArrayList<>(Arrays.asList(removeExtensions(files[i - 1].getName()), removeExtensions(files[i].getName()))));
                System.out.println((i -1 ) + " -> " +files[i - 1].getName() + " with " + i + " -> " +files[i].getName());
            }
            createDataLog();
        }
    }

    public boolean compare2Images() {
        File file = new File(inputFile1);
        File file2 = new File(inputFile2);

        return ImageCompare.compareWithBaseImage(file, file2);
    }

    private String removeExtensions(String string) {
        return string.substring(0,string.length() - 4);
    }

    private void createDataLog() {
        try {
            BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter("data.txt"));

            String text = "";
            for (ArrayList<String> data: deletedImages) {
                text += data.get(0) + "," + data.get(1) + ":" + System.getProperty("line.separator");
            }

            writer.write(text);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void restoreImages(String dirFiles,String extension) {
        ArrayList<ArrayList<String>> fileNames = readText("data.txt");
        for (ArrayList<String> names:  fileNames) {

            if(names.size() == 2) {
                try {
                    Files.copy(Paths.get(dirFiles + "\\" + names.get(0) + "." + extension),Paths.get(dirFiles + "\\" + names.get(1) + "." + extension), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR");
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    private ArrayList<ArrayList<String>> readText(String file)  {
        String data = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), Charset.forName("UTF-8"))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                data += currentLine;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        // char to split first : :
        // char to split next : ,
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        for (String text :  data.split(":")) {
            String[] textSplit = text.split(",");
            dataList.add(new ArrayList<>(Arrays.asList(textSplit[0],textSplit[1])));
        }

        return dataList;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory + "\\";
    }

    public void setInputFile1(String inputFile1) {
        this.inputFile1 = inputFile1;
    }

    public void setInputFile2(String inputFile2) {
        this.inputFile2 = inputFile2;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
