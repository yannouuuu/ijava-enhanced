package extensions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/* loaded from: ijava.jar:extensions/File.class */
public class File {
    private BufferedReader input;

    public File(String str) {
        try {
            this.input = new BufferedReader(new FileReader(str));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ready() {
        try {
            return this.input.ready();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readLine() {
        try {
            return this.input.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
