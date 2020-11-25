package ptui;

//imports
import javafx.application.Platform;
import model.CustomException;
import model.RITMain;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * uncompresses a rit file and writes a uncompressed txt file
 *
 * @author David Millard
 */
public class RITUncompress {

    // class variables
    private String compressedRead;
    private String uncompressedWrite;
    private RITMain model;

    /**
     *
     * constructor for ptui uncompress
     * takes the args and intitializes
     *
     * @param compressedRead file to uncompresss
     * @param uncompressedWrite file to write to
     */
    public RITUncompress(String compressedRead, String uncompressedWrite) {
        this.compressedRead = compressedRead;
        this.uncompressedWrite = uncompressedWrite;
        initialize();
    }

    /**
     *
     * runs when program is run
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }
        new RITUncompress(args[0], args[1]);
    }

    /**
     *
     * the main run for the function, tests errors and runs functionality
     *
     */
    private void initialize() {
        ArrayList<Integer> arr_i = new ArrayList<>();
        try {
            try {
                Scanner in = new Scanner(new FileReader(this.compressedRead));

                FileWriter file = new FileWriter(uncompressedWrite);
                file.close();

                while (true) {
                    int temp = Integer.parseInt(in.nextLine());
                    arr_i.add(temp);
                }

            } catch (FileNotFoundException e) {
                throw new CustomException("File not found.");
            } catch (IOException e) {
                throw new CustomException("File cannot be written to.");
            } catch (NoSuchElementException ignored) {
                this.model = new RITMain(arr_i, 1);
                writeToFile();
                displayStatistics();
            }
        } catch (CustomException e) {
            System.out.println(e.toString());
            Platform.exit();
        }
    }

    /**
     *
     * writes the uncompressed file
     *
     * @throws CustomException if file is not found
     */
    private void writeToFile() throws CustomException {
        try {
            FileWriter file = new FileWriter(uncompressedWrite);
            int[][] imageMatrix = this.model.getImageMatrix();
            int dim = model.getDIM();
            for (int x = 0; x < dim; x++) {
                for (int y = 0; y < dim; y++) {
                    String temp = Integer.toString(imageMatrix[y][x]);
                    file.write(temp + "\n");
                }
            }
            file.close();
        } catch (IOException e) {
            throw new CustomException("File cannot be written to.");
        }
    }

    /**
     *
     * display the stats of the umcompressing and run
     *
     */
    private void displayStatistics() {
        System.out.println("Uncompressing: " + compressedRead);
        System.out.print("QTree: ");
        model.printTree(model.getNode());
        System.out.println("\nOutput file: " + uncompressedWrite);
    }
}