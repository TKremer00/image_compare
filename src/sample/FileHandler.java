package sample;

import javafx.beans.property.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

public class FileHandler extends Thread {

    private String fileExtension;
    private ArrayList<ArrayList<String>> deletedImages;
    private ArrayList<File> files;
    private BooleanProperty finishedCompare;
    private DoubleProperty progress;
    private int totalImages;
    private int method;
    public static StringProperty log = new SimpleStringProperty();
    private Thread thread;
    private Runtime runtime = Runtime.getRuntime();
    private int numberOfProcessors = runtime.availableProcessors();
    public static int threadOpen = 0;

    public FileHandler() {
        deletedImages = new ArrayList<>();
        finishedCompare = new SimpleBooleanProperty(false);
        progress = new SimpleDoubleProperty(0);
    }

    public boolean deleteSameImages(String inputDirectory) {
        // Delete images
        for (ArrayList<String> data: deletedImages) {
            File file = new File(inputDirectory + data.get(1) + "." + fileExtension);
            if(!file.delete())
                return false;
        }
        return true;
    }

    public void compareImages(String inputDirectory, int method) {
        File dir = new File(inputDirectory);
        files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles((d, name) -> name.endsWith("." + fileExtension)))));
        totalImages = files.size();
        this.method = method;
        thread = new Thread(this);
        thread.start();
    }

    public boolean compare2Images(String inputFile1, String inputFile2) {
        File file = new File(inputFile1);
        File file2 = new File(inputFile2);

        return ImageCompare.compareImage(file, file2);
    }

    private void checkNeighbors(){
        log.setValue("");
        for (int i = 1; i < files.size(); i++) {
            log.setValue(files.get(i-1).getName() + "\n" + log.getValue());
            if(ImageCompare.compareImage(files.get(i -1), files.get(i)))
                deletedImages.add(new ArrayList<>(Arrays.asList(removeExtensions(files.get(i-1).getName()), removeExtensions(files.get(i).getName()))));
            progress.set(progress.get() + 1);
        }
    }

    private void checkLiniar(){
        log.setValue("");
        ArrayList<File> check = new ArrayList<>(Collections.singletonList(files.get(files.size() - 1)));
        int add = 0;
        for (int i = files.size() - 2; i  > 0; i--) {
            long start = System.nanoTime();
            boolean compares = false;
            for (File file: check) {
                if(ImageCompare.compareImage(file, files.get(i))){
                    deletedImages.add(new ArrayList<>(Arrays.asList(removeExtensions(file.getName()), removeExtensions(files.get(i).getName()))));
                    compares = true;
                    break;
                }
            }
            String name = files.get(i).getName();

            progress.set(progress.get() + 1);
            if(!compares){
                check.add(files.get(i));
            }else{
                name += " x";
            }
            long end = System.nanoTime();
            log.setValue(name + " - "+ ((end - start) / 1000000) +"\n" + log.getValue());
        }
    }

    @Override
    public void run() {
        deletedImages = new ArrayList<>();
        if(method == 0){
            System.out.println("opend thread");
            //threadOpen++;
            checkNeighbors();
        }else if(method == 1){
            checkLiniar();
        }
        finishedCompare.set(true);
        createDataLog();

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
                text.append(data.get(0)).append(",").append(data.get(1)).append(":").append(System.getProperty("line.separator"));
            }

            Runtime.getRuntime().exec("explorer.exe /select, " + System.getProperty("user.dir") + "\\data.txt");

            writer.write(text.toString());
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreImages(String dirFiles,String extension) {
        ArrayList<ArrayList<String>> fileNames = readText(System.getProperty("user.dir") + "\\data.txt");

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
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
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

    public BooleanProperty finishedCompareProperty() {
        return finishedCompare;
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public int getTotalImages() {
        return totalImages;
    }
}
