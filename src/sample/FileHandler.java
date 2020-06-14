package sample;

import javafx.beans.property.*;
import views.CompareGui;
import views.Gui;

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
    private Thread thread;

    private Runtime runtime = Runtime.getRuntime();
    private int numberOfProcessors = runtime.availableProcessors();
    private static int threadOpen = 0;
    private static int threadCount = 0;
    private static long lastThreadId = 0;

    public FileHandler() {
        deletedImages = new ArrayList<>();
        finishedCompare = new SimpleBooleanProperty(false);
        progress = new SimpleDoubleProperty(0);
    }

    public boolean deleteSameImages(String inputDirectory) {
        // Delete images
        ArrayList<ArrayList<String>> deletedImages = CheckedFiles.getDoubles();
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
        CompareGui.log.setValue("");
        CheckedFiles.ClearLists();
        for (int i = 1; i < files.size(); i++) {
            CompareGui.log.setValue(files.get(i-1).getName() + "\n" + CompareGui.log.getValue());
            if(ImageCompare.compareImage(files.get(i -1), files.get(i)))
                CheckedFiles.addToListDouble(new ArrayList<>(Arrays.asList(removeExtensions(files.get(i-1).getName()), removeExtensions(files.get(i).getName()))));
            progress.set(progress.get() + 1);
        }
    }

    private void checkLiniar()
    {
        if(threadOpen == 0){
            CompareGui.log.setValue("");
            CheckedFiles.ClearLists();
        }

        for (int i = files.size() - 2; i > 0; i--)
        {
            long start = System.nanoTime();
            if(CheckedFiles.Checked(files.get(i + 1))){
                continue;
            }

            CheckedFiles.addToListChecked(files.get(i+1));

            if(!CheckedFiles.Checked(files.get(i)) &&ImageCompare.compareImage(files.get(i + 1), files.get(i))){
                CheckedFiles.addToListChecked(files.get(i));
                if(CheckedFiles.writing){
                    while (CheckedFiles.writing){
                        // block the thread
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
                CheckedFiles.addToListDouble(new ArrayList<>(Arrays.asList(removeExtensions(files.get(i+1).getName()), removeExtensions(files.get(i).getName()))));
            }else if(threadOpen < numberOfProcessors){
                Thread thread = new Thread(this);
                thread.start();
                lastThreadId = thread.getId();
            }

            // if progress 100 popup opens
            progress.set(progress.get() + 1);

            long end = System.nanoTime();
            String text = files.get(i + 1).getName() + " - "+ ((end - start) / 1000000) + "ms";
            Gui.compareGui.AddToLog(text);
        }
        threadCount--;
    }

    @Override
    public void run() {
        deletedImages = new ArrayList<>();
        if(method == 0){
            checkNeighbors();
        }else if(method == 1){
            threadOpen++;
            threadCount++;
            checkLiniar();
        }

        if(threadCount == 0){
            finishedCompare.set(true);
            createDataLog();
        }

    }

    private String removeExtensions(String string) {
        return string.substring(0,string.length() - 4);
    }

    private void createDataLog() {
        try {
            BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter("data.txt"));
            StringBuilder text = new StringBuilder();
            ArrayList<ArrayList<String>> deletedImages = CheckedFiles.getDoubles();
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
