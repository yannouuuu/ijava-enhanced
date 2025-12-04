package extensions;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* loaded from: ijava.jar:extensions/CSVFile.class */
public class CSVFile {
    protected List<List<String>> table;

    public CSVFile(String str, char c) {
        this.table = new ArrayList();
        try {
            Scanner scanner = new Scanner(new FileReader(str));
            while (scanner.hasNext()) {
                Scanner useDelimiter = new Scanner(scanner.nextLine()).useDelimiter(c);
                ArrayList arrayList = new ArrayList();
                while (useDelimiter.hasNext()) {
                    arrayList.add(useDelimiter.next());
                }
                this.table.add(arrayList);
                useDelimiter.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CSVFile(String str) {
        this(str, ',');
    }

    public int rowCount() {
        return this.table.size();
    }

    public int columnCount() {
        return columnCount(0);
    }

    public int columnCount(int i) {
        return this.table.get(i).size();
    }

    public String getCell(int i, int i2) {
        return this.table.get(i).get(i2);
    }

    public static void save(String[][] strArr, String str, char c) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(str));
        for (int i = 0; i < strArr.length; i++) {
            for (int i2 = 0; i2 < strArr[0].length - 1; i2++) {
                bufferedWriter.write(strArr[i][i2] + c);
            }
            bufferedWriter.write(strArr[i][strArr[0].length - 1] + "\n");
        }
        bufferedWriter.close();
    }

    public void dump() {
        for (int i = 0; i < rowCount(); i++) {
            for (int i2 = 0; i2 < columnCount(); i2++) {
                System.out.print(getCell(i, i2) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] strArr) throws IOException {
        if (strArr.length == 0 || strArr.length > 2) {
            System.err.println("Usage: java SimpleTable nameOfaCSVfile [separator]");
            System.exit(1);
        }
        char c = ',';
        if (strArr.length == 2) {
            c = strArr[1].charAt(0);
        } else {
            System.err.println("No separator provided: using comma by default");
        }
        CSVFile cSVFile = new CSVFile(strArr[0], c);
        cSVFile.dump();
        System.out.println("There are " + cSVFile.rowCount() + " lines with " + cSVFile.columnCount() + " columns.");
        String[][] strArr2 = new String[cSVFile.rowCount()][2];
        for (int i = 0; i < cSVFile.rowCount(); i++) {
            strArr2[i][0] = cSVFile.getCell(i, 0);
            strArr2[i][1] = cSVFile.getCell(i, 1);
        }
        save(strArr2, "Test.csv", ';');
    }
}
