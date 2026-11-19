package org.h2.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.h2.store.fs.FileUtils;

/* loaded from: ijava.jar:org/h2/util/SortedProperties.class */
public class SortedProperties extends Properties {
    private static final long serialVersionUID = 1;

    @Override // java.util.Hashtable, java.util.Dictionary
    public synchronized Enumeration<Object> keys() {
        ArrayList arrayList = new ArrayList();
        Iterator it = keySet().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toString());
        }
        arrayList.sort(null);
        return Collections.enumeration(arrayList);
    }

    public static boolean getBooleanProperty(Properties properties, String str, boolean z) {
        try {
            return Utils.parseBoolean(properties.getProperty(str, null), z, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return z;
        }
    }

    public static int getIntProperty(Properties properties, String str, int i) {
        try {
            return Integer.decode(properties.getProperty(str, Integer.toString(i))).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    public static String getStringProperty(Properties properties, String str, String str2) {
        return properties.getProperty(str, str2);
    }

    public static synchronized SortedProperties loadProperties(String str) throws IOException {
        SortedProperties sortedProperties = new SortedProperties();
        if (FileUtils.exists(str)) {
            InputStream newInputStream = FileUtils.newInputStream(str);
            try {
                sortedProperties.load(new InputStreamReader(newInputStream, StandardCharsets.ISO_8859_1));
                if (newInputStream != null) {
                    newInputStream.close();
                }
            } catch (Throwable th) {
                if (newInputStream != null) {
                    try {
                        newInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        return sortedProperties;
    }

    public synchronized void store(String str) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        store(byteArrayOutputStream, (String) null);
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), StandardCharsets.ISO_8859_1));
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(FileUtils.newOutputStream(str, false), StandardCharsets.ISO_8859_1)));
            while (true) {
                try {
                    String readLine = lineNumberReader.readLine();
                    if (readLine != null) {
                        if (!readLine.startsWith("#")) {
                            printWriter.print(readLine + "\n");
                        }
                    } else {
                        printWriter.close();
                        return;
                    }
                } catch (Throwable th) {
                    try {
                        printWriter.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        } catch (Exception e) {
            throw new IOException(e.toString(), e);
        }
    }

    public synchronized String toLines() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : new TreeMap(this).entrySet()) {
            sb.append(entry.getKey()).append('=').append(entry.getValue()).append('\n');
        }
        return sb.toString();
    }

    public static SortedProperties fromLines(String str) {
        SortedProperties sortedProperties = new SortedProperties();
        for (String str2 : StringUtils.arraySplit(str, '\n', true)) {
            int indexOf = str2.indexOf(61);
            if (indexOf > 0) {
                sortedProperties.put(str2.substring(0, indexOf), str2.substring(indexOf + 1));
            }
        }
        return sortedProperties;
    }
}
