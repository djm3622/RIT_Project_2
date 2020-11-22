package gui;

//imports
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.CustomException;
import model.RITMain;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * displays the image of an uncompressed image file
 *
 * @author David Millard
 */
public class RITViewer extends Application {
    private static String[] arg;
    private RITMain model;

    /**
     *
     * initialize and check for errors
     *
     */

    @Override
    public void init() {
        ArrayList<Integer> arr_i = new ArrayList<>();

        try {
            try {
                if (arg.length != 1) {
                    throw new CustomException("Invalid amount of arguments.");
                }
                Scanner in = new Scanner(new FileReader(arg[0]));
                while (true) {
                    int temp = Integer.parseInt(in.nextLine());
                    if (temp > 255 || temp < 0) {
                        throw new CustomException("Pixel value outside the range 0-255.");
                    }
                    arr_i.add(temp);
                }
            } catch (FileNotFoundException e) {
                throw new CustomException("File not found.");
            } catch (NumberFormatException e) {
                throw new CustomException("Pixel value not an integer.");
            } catch (NoSuchElementException ignored) {
                model = new RITMain(arr_i, 0);

                if (!model.checkForSquare(model.getDIM())) {
                    throw new CustomException("Dimensions not a square.");
                }
                model.createImageMatrix();
            }
        } catch (CustomException e) {
            System.out.println(e.toString());
            Platform.exit();
        }
    }

    /**
     *
     * create gui elemetns and display
     *
     * @param stage stage of all elements
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("RITViewer");
        Canvas canvas = new Canvas(model.getDIM(), model.getDIM());
        Group group = new Group();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        createImage(gc);

        group.getChildren().add(canvas);
        stage.setScene(new Scene(group));

        stage.show();
    }

    /**
     *
     * set the gc with the 2 dimensional array
     *
     * @param gc the graphic context to put picture
     */
    private void createImage(GraphicsContext gc) {
        int x = 0;
            for (int[] e : model.getImageMatrix()) {
                int y = 0;
                for (int i: e) {
                    double d = i / 255.0;
                    gc.setFill(new Color(d, d, d, 1.0));
                    gc.fillRect(x, y, 1, 1);
                    y++;
                }
                x++;
            }
    }

    /**
     *
     * gets main arguments and launches gui elemetns
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        arg = args;
        Application.launch(args);
    }
}
