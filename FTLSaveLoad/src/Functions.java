import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by IntelliJ IDEA.
 * User: rrd09
 * Date: 28/09/12
 * Time: 10:48
 */
public class Functions {

    private static String rootDirectory = "C:\\Users\\Ren\\Documents\\My Games\\FasterThanLight\\";
    private static String saveName = "continue";
    private static String fileExtension = ".sav";
    private static int counter;

    public static void copyFile(File from, File to) throws IOException {
        Files.delete(to.toPath());
        Files.copy(from.toPath(), to.toPath());
    }

    public static void save() throws IOException {
        scan();
        File from = new File(rootDirectory + saveName + fileExtension);
        counter++;
        File to = new File(rootDirectory + counter + fileExtension);
        if (!to.exists()) to.createNewFile();
        copyFile(from, to);
        System.out.println("Saved to " + counter + fileExtension);
        call();
    }

    public static void load() throws IOException {
        scan();
        load(counter);
    }

    public static void load(int number) throws IOException {
        File from = new File(rootDirectory + number + fileExtension);
        File to = new File(rootDirectory + saveName + fileExtension);
        if (!to.exists()) to.createNewFile();
        copyFile(from, to);
        System.out.println("Loaded " + number + fileExtension);
        call();
    }

    public static void listFiles() {
        File directory = new File(rootDirectory);
        String[] children = directory.list();
        if (children == null) {
        } else {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
                System.out.println(filename);
            }
        }
    }

    public static void scan() {
        File directory = new File(rootDirectory);
        String[] children = directory.list();
        counter = 0;
        if (children == null) {
        } else {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
                //System.out.println(filename);
                filename = filename.replaceAll(fileExtension, "");
                try {
                    int number = Integer.parseInt(filename);
                    if (number > counter)
                        counter = number;
                } catch (NumberFormatException e) {
                }
            }
            //System.out.println("Current Counter = " + counter);
        }
    }

    public static void call() {
        try {
            Runtime.getRuntime().exec
                    ("cmd /c start " + "C:\\" + "/FTL.url");
        }

        catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Oups", JOptionPane.ERROR_MESSAGE);
        }
    }
}
