package me.mck9061;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<Specimen> specimenList;
    public static void main(String[] args) throws FileNotFoundException {
        String location = "data";
        specimenList = new ArrayList<>();

        File dir = new File(location);
        for (File file : dir.listFiles()) {
            Specimen specimen = new Specimen(file);
            specimenList.add(specimen);
        }

        File valueFile = new File("values.txt");
        Scanner scanner = new Scanner(valueFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String name = line.split("=")[0];
            int value = Integer.parseInt(line.split("=")[1]);

            for (Specimen specimen : specimenList) {
                if ((specimen.genus + " " + specimen.species).equals(name)) specimen.value = value;
            }
        }

        File dirFile = new File("directory.txt");
        Scanner dirScanner = new Scanner(dirFile);
        EdJournal journal = new EdJournal(getLastModified(dirScanner.nextLine()).getPath());
        System.out.println("Journal directory: " + journal.fileLocation);

        Scanner scanner1 = new Scanner(System.in);

        while (!scanner1.nextLine().equals("end")) {
            journal.findNext();
        }
    }


    public static File getLastModified(String directoryFilePath)
    {
        File directory = new File(directoryFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null)
        {
            for (File file : files)
            {
                if (file.lastModified() > lastModifiedTime && file.getName().contains("Journal."))
                {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }
}