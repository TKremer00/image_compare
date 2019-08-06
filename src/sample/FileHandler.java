package sample;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {

    private String fileExtension;
    private ArrayList<ArrayList<String>> deletedImages = new ArrayList<>();

    public boolean deleteSameImages(String inputDirectory) {
        // Delete images
        for (ArrayList<String> data: deletedImages) {
            File file = new File(inputDirectory + data.get(1) + "." + fileExtension);
            if(!file.delete()) {
                return false;
            }
        }
        return true;
    }

    public void compareImages(String inputDirectory) {
        File dir = new File(inputDirectory);
        File[] files = dir.listFiles((d, name) -> name.endsWith("." + fileExtension));

        for (int i = 1; i < files.length; i++) {
            if(ImageCompare.compareImage(files[i - 1], files[i]))
                deletedImages.add(new ArrayList<>(Arrays.asList(removeExtensions(files[i - 1].getName()), removeExtensions(files[i].getName()))));
        }
        createDataLog();
    }

    public boolean compare2Images(String inputFile1, String inputFile2) {
        File file = new File(inputFile1);
        File file2 = new File(inputFile2);

        return ImageCompare.compareImage(file, file2);
    }

    private String removeExtensions(String string) {
        return string.substring(0,string.length() - 4);
    }

    private void createDataLog() {
        try {
            BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter("data.txt"));
            StringBuilder text = new StringBuilder();

            for (ArrayList<String> data: deletedImages) {
                text.append(data.get(0) + "," + data.get(1) + ":" + System.getProperty("line.separator"));
            }

            writer.write(text.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreImages(String dirFiles,String extension) {
        ArrayList<ArrayList<String>> fileNames = readText("data.txt");

        for (ArrayList<String> names : fileNames) {
            if(names.size() == 2) {
                try {
                    Files.copy(
                            Paths.get(dirFiles + "\\" + names.get(0) + "." + extension),
                            Paths.get(dirFiles + "\\" + names.get(1) + "." + extension),
                            StandardCopyOption.COPY_ATTRIBUTES);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private ArrayList<ArrayList<String>> readText(String file)  {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), Charset.forName("UTF-8"))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                data.append(currentLine);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // char to split first : :
        // char to split next : ,
        ArrayList<ArrayList<String>> dataList = new ArrayList<>();
        for (String text :  data.toString().split(":")) {
            String[] textSplit = text.split(",");
            dataList.add(new ArrayList<>(Arrays.asList(textSplit[0],textSplit[1])));
        }

        return dataList;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
