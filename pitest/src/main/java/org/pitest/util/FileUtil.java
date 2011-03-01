package org.pitest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

  public static boolean deleteDirectory(final File path) {
    if (path.exists()) {
      final File[] files = path.listFiles();
      for (final File file : files) {
        if (file.isDirectory()) {
          deleteDirectory(file);
        } else {
          file.delete();
        }
      }
    }
    return (path.delete());
  }

  public static String readToString(final InputStream is)
      throws java.io.IOException {
    final StringBuffer fileData = new StringBuffer(1000);
    final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    char[] buf = new char[1024];
    int numRead = 0;

    while ((numRead = reader.read(buf)) != -1) {
      final String readData = String.valueOf(buf, 0, numRead);
      fileData.append(readData);
      buf = new char[1024];
    }

    reader.close();
    return fileData.toString();
  }

}
