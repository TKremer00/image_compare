package sample;

import java.util.ArrayList;
import java.io.File;
import java.util.Comparator;

class CheckedFiles {

    static boolean writing = false;
    private static ArrayList<File> files = new ArrayList<>();


    private static ArrayList<ArrayList<String>> doubles = new ArrayList<>();

    static void addToListChecked(File file)
    {
        files.add(file);
    }

    static void addToListDouble(ArrayList<String> files)
    {
        if(!doubles.contains(files)){
            writing = true;
            for (ArrayList<String> aDouble : doubles) {
                if (files.get(1).equals(aDouble.get(0))) {
                    aDouble.set(0, files.get(0));
                }
            }
            doubles.add(files);
            writing = false;
        }
    }

    static void ClearLists()
    {
        doubles = new ArrayList<>();
        files = new ArrayList<>();
    }

    static ArrayList<ArrayList<String>> getDoubles(){
        Comparator<ArrayList<String>> compareSeccond = Comparator.comparing((ArrayList<String> o) -> o.get(1));
        Comparator<ArrayList<String>> compareFirst = Comparator.comparing((ArrayList<String> o) -> o.get(0));

        doubles.sort(compareSeccond.reversed());
        doubles.sort(compareFirst.reversed());
        ArrayList<ArrayList<String>> old_doubles = doubles;
        for (int i = 0; i < old_doubles.size(); i++) {
            ArrayList<String> checker = old_doubles.get(i);
            for (int j = i + 1; j < old_doubles.size(); j++) {
                if(checker.get(1).equals(old_doubles.get(j).get(0))){
                    doubles.get(j).set(0,checker.get(0));
                }
            }
        }
        return doubles;
    }

    static boolean Checked(File file)
    {
        return files.contains(file);
    }
}
