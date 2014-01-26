import java.io.*;
import java.lang.*;
import java.util.*;

public class LOSOFileGenerator {

    //static int numObjects;
    static int numAttrs;
    static ArrayList<String> dataset;
    static String[][] datasetArray;

    /**
     * Fetch the entire contents of a text file, and return it in a String.
     * This style of implementation does not throw Exceptions to the caller.
     *
     * @param aFile is a file which already exists and can be read.
     */
    private static String[][] getContents(File aFile) throws IOException {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();
        //numObjects = 0;
        numAttrs = 0;
        ArrayList<String> testSet;
        ArrayList<String> trainingSet;
        ArrayList<String> arffHeader;

        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            dataset = new ArrayList<String>();

            try {
                String line = null; //not declared within while loop
                /*
                * readLine is a bit quirky :
                * it returns the content of a line MINUS the newline.
                * it returns null only for the END of the stream.
                * it returns an empty String if two newlines appear in a row.
                */

                while ((line = input.readLine()) != null) {
                    dataset.add(line);
                    System.out.println(line);
                    //numObjects += 1;
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(dataset.size());

        //set data array 'x' size
        String anObj = dataset.get(0);
        StringTokenizer st = new StringTokenizer(anObj);
        numAttrs = st.countTokens();
        System.out.println(numAttrs);

        //generate headers
        arffHeader = new ArrayList<String>();
        arffHeader.add("@RELATION dataset");
        for (int a = 0; a < numAttrs - 1; a++) {
            arffHeader.add("@ATTRIBUTE " + a + "NUMERIC");
        }
        arffHeader.add("@ATTRIBUTE decision {0, 1}");
        arffHeader.add("@DATA");

        //build 2d array from the arrayList

        //instantiate the dataset array
        datasetArray = new String[dataset.size()][numAttrs];
        for (int x = 0; x < dataset.size(); x++) {

            String theObj = dataset.get(x);
            StringTokenizer st2 = new StringTokenizer(theObj);
            int q = 0;
            while (st2.hasMoreTokens()) {
                System.out.println(datasetArray[x][q] = st2.nextToken());
                q++;
            }

        }

        ArrayList<String> IDArrayList = new ArrayList<String>();
        //Iterate over the array and get the ID's
        System.out.println("Rows: " + datasetArray.length);
        for (int y = 0; y < dataset.size(); y++) {
            System.out.println("Y= " + y);
            String ID = datasetArray[y][numAttrs - 1];
            if (!IDArrayList.contains(ID)) {
                //System.out.println("ID" + ID);
                IDArrayList.add(ID);
            }
        }

        //Loop through the IDs and extract the objects from the dataset
        for (int ida = 0; ida < IDArrayList.size(); ida++) {
            String objID = IDArrayList.get(ida);
            System.out.println(objID);
            testSet = new ArrayList<String>();
            trainingSet = new ArrayList<String>();
            //Loop through the dataset
            for (int ds = 0; ds < datasetArray.length; ds++) {
                if (objID.equals(datasetArray[ds][numAttrs - 1])) {
                    StringBuffer theObj = new StringBuffer();
                    //loop through the object and get everything out except the ID
                    for (int attr = 0; attr < numAttrs - 1; attr++) {
                        theObj.append(datasetArray[ds][attr] + " ");
                    }
                    //add the object to the testset array
                    testSet.add(theObj.toString());
                } else {
                    StringBuffer theObj2 = new StringBuffer();
                    //loop through the object and get everything out except the ID
                    for (int attr = 0; attr < numAttrs - 1; attr++) {
                        theObj2.append(datasetArray[ds][attr] + " ");
                    }
                    trainingSet.add(theObj2.toString());
                }
            }

            //write test & training sets to files
            Writer testOutput = new OutputStreamWriter(new FileOutputStream("testFile_" + objID + ".arff"));
            Writer trainOutput = new OutputStreamWriter(new FileOutputStream("trainFile_" + objID + ".arff"));
            for (String s : arffHeader) {
                testOutput.write(s + System.getProperty("line.separator"));
                trainOutput.write(s + System.getProperty("line.separator"));
            }
            for (String s : testSet) testOutput.write(s + System.getProperty("line.separator"));
            for (String s : trainingSet) trainOutput.write(s + System.getProperty("line.separator"));
            testOutput.close();
            trainOutput.close();
        }
        return null;
    }

    /**
     * Change the contents of text file in its entirety, overwriting any
     * existing text.
     * <p/>
     * This style of implementation throws all exceptions to the caller.
     *
     * @param aFile is an existing file which can be written to.
     * @throws IllegalArgumentException if param does not comply.
     * @throws FileNotFoundException    if the file does not exist.
     * @throws IOException              if problem encountered during write.
     */
    static public void setContents(File aFile, String aContents) throws FileNotFoundException, IOException {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        if (!aFile.exists()) {
            throw new FileNotFoundException("File does not exist: " + aFile);
        }
        if (!aFile.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        if (!aFile.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + aFile);
        }

        //use buffering
        Writer output = new BufferedWriter(new FileWriter(aFile));
        try {
            //FileWriter always assumes default encoding is OK!
            output.write(aContents);
        } finally {
            output.close();
        }
    }

    /**
     * Simple test harness.
     */
    public static void main(String args[]) throws IOException {
        File testFile = new File("Test.txt");
        getContents(testFile);
        //System.out.println("Original file contents: " + getContents(testFile));
        //buildARFF();
        //setContents(testFile, "The content of this file has been
//        overwritten...");
        //System.out.println("New file contents: " + getContents(testFile));
    }
}