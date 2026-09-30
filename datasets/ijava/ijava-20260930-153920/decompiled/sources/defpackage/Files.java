package defpackage;

import extensions.CSVFile;
import extensions.File;

/* loaded from: ijava.jar:Files.class */
public interface Files {
    String[] getAllFilesFromDirectory(String str);

    String[] getAllFilesFromCurrentDirectory();

    boolean newDirectory(String str);

    File newFile(String str);

    boolean ready(File file);

    String readLine(File file);

    CSVFile loadCSV(String str);

    CSVFile loadCSV(String str, char c);

    int rowCount(CSVFile cSVFile);

    int columnCount(CSVFile cSVFile);

    int columnCount(CSVFile cSVFile, int i);

    String getCell(CSVFile cSVFile, int i, int i2);

    void saveCSV(String[][] strArr, String str);

    void saveCSV(String[][] strArr, String str, char c);
}
