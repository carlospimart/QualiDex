/*
 * Copyright (c) 2017-2018. QualiTest Software Testing Limited.
 */
package main.java.com.qualitestgroup.dataextract.utilities;

import java.io.File;
import java.net.URL;

/**
 * @author Daniel Geater
 */
public class FileHelper {
    public File openFile(String filename) {
        File file = openFileFromClassPath(filename);
        if (file == null) {
            file = openFileFromFilesystem(filename);
        }
        return file;
    }

    /**
     * Attempt to locate a file on the classpath using details provided
     *
     * @param filename - filename to locate
     * @return handle on the file if it exists, null if it does not.
     */
    public File openFileFromClassPath(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(filename);
        if (url == null) {
            return null;
        }
        File file = new File(url.getFile().replace("%20", " "));
        if (!file.exists()) {
            return null;
        } else {
            return file;
        }

    }

    /**
     * Attempt to locate a file in the File system using details provided
     *
     * @param filename - filename to locate
     * @return handle on the file if it exists, null if it does not.
     */
    public File openFileFromFilesystem(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        } else {
            return file;
        }
    }
}
