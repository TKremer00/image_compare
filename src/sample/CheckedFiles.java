package sample;

import java.util.ArrayList;
import java.io.File;

public class CheckedFiles {

    private static ArrayList<File> files = new ArrayList<>();

    public static void addToList(File file)
    {
        files.add(file);
    }

    public static boolean Checked(File file)
    {
        return files.contains(file);
    }
}
