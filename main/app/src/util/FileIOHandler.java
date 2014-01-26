package util;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 01/12/11
 * Time: 12:15
 * To change this template use File | Settings | File Templates.
 */
public class FileIOHandler {
    private String rootDirectory = "";

    public FileIOHandler(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public FileIOHandler() {
    }

    public BufferedReader getReader(String fileName) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(rootDirectory + fileName));
        return reader;
    }

    public BufferedWriter getWriter(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(rootDirectory + fileName));
        return writer;
    }
}
