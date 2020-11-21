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

public class RITUncompress {

    private String compressedRead;
    private String uncompressedWrite;
    private RITMain model;


    public RITUncompress(String compressedRead, String uncompressedWrite) {
        this.compressedRead = compressedRead;
        this.uncompressedWrite = uncompressedWrite;
        initialize();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            return;
        }
        RITUncompress ptui = new RITUncompress(args[0], args[1]);
    }

    private void initialize() {
        ArrayList<Integer> arr_i = new ArrayList<>();
        try {
            try {
                Scanner in = new Scanner(new FileReader(this.compressedRead));

                while (true) {
                    int temp = Integer.parseInt(in.nextLine());
                    arr_i.add(temp);
                }

            } catch (FileNotFoundException e) {
                throw new CustomException("File not found.");
            } catch (NoSuchElementException ignored) {
                this.model = new RITMain(arr_i, 1);
                createFile();
            }
        } catch (CustomException e) {
            System.out.println(e.toString());
            Platform.exit();
        }
    }

    private void createFile() throws CustomException {
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
}