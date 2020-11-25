package ptui;

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

public class RITCompress {
    // class variables
    private String uncompressedRead;
    private String compressedWrite;
    private RITMain model;

    /**
     *
     * constructor for ptui compress
     * takes the args and intitializes
     *
     * @param uncompressedRead file to compress
     * @param compressedWrite file to write to
     */
    public RITCompress(String uncompressedRead, String compressedWrite) {
        this.uncompressedRead = uncompressedRead;
        this.compressedWrite = compressedWrite;
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
            System.out.println("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
            return;
        }
        new RITCompress(args[0], args[1]);
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
                Scanner in = new Scanner(new FileReader(this.uncompressedRead));

                FileWriter file = new FileWriter(compressedWrite);
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
                this.model = new RITMain(arr_i, 2);
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
     * writes the compressed file
     *
     * @throws CustomException if file is not found
     */
    private void writeToFile() throws CustomException {
        try {
            FileWriter file = new FileWriter(compressedWrite);
            file.write(model.getDepth() + "\n");
            writeTree(model.getNode(), file);
            file.close();
        } catch (IOException e) {
            throw new CustomException("File cannot be written to.");
        }
    }

    /**
     *
     * display the stats of the compressing and run
     *
     */
    private void displayStatistics() {
        System.out.println("Compressing: " + uncompressedRead);
        System.out.print("QTree: ");
        model.printTree(model.getNode());
        System.out.println("\nOutput file: " + compressedWrite);
        System.out.println("Raw image size: " + model.getDepth());
        System.out.println("Compressed image size: " + model.getCompressDepth());
        System.out.println("Compression %: " + (((1 - ((double) model.getCompressDepth() / (double) model.getDepth()))) * 100));
    }

    /**
     *
     * preorder traversal of rit tree
     *
     * @param node rit tree
     */
    private void writeTree(model.RITQTNode node, FileWriter file) throws IOException {
        if (node != null) {
            file.write(node.getVal() + "\n");
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case (0) -> writeTree(node.getUpperLeft(), file);
                    case (1) -> writeTree(node.getUpperRight(), file);
                    case (2) -> writeTree(node.getLowerLeft(), file);
                    case (3) -> writeTree(node.getLowerRight(), file);
                }
            }
        }
    }
}
