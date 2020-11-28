package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.CustomException;
import model.Observer;
import model.RITMain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * gui that carries out all fucntion of rit compress, uncompress, and view
 *
 * @author David Millard
 */

public class RITGUI extends Application implements Observer<RITMain> {

    private RITMain model;
    private String inputFile;
    private String outputFile;
    private String errorMessage;
    private GraphicsContext gc;
    private Canvas canvas;

    /**
     *
     * sets default parameters and begins model observation
     *
     */
    @Override
    public void init() {
        this.model = new RITMain();
        model.addObserver(this);
        errorMessage = "";
    }

    /**
     *
     * create gui elemetns and display
     *
     * @param stage stage of all elements
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("RITGUI");

        BorderPane bPane = new BorderPane();

        canvas = new Canvas(0, 0);
        Group group = new Group();
        gc = canvas.getGraphicsContext2D();

        FileChooser fileChooser = new FileChooser();
        Button inputButton = new Button("Input File");
        Button outputButton = new Button("Output File");

        Label inputLBL = new Label("");
        inputLBL.setMinWidth(600);
        inputLBL.setStyle("-fx-border-color: grey;");
        Label outputLBL = new Label("");
        outputLBL.setMinWidth(600);
        outputLBL.setStyle("-fx-border-color: grey;");

        Label bottomMessage = new Label("");

        inputButton.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        inputFile = file.getAbsolutePath();
                        inputLBL.setText(inputFile);
                    }
                });

        outputButton.setOnAction(
                e -> {
                    List<File> list =
                            fileChooser.showOpenMultipleDialog(stage);
                    if (list != null) {
                        for (File file : list) {
                            outputFile = file.getAbsolutePath();
                            outputLBL.setText(outputFile);
                        }
                    }
                });

        MenuButton menuButton = new MenuButton("Operation");

        MenuItem compressButton = new MenuItem("Compress");
        compressButton.setOnAction(
                e -> {
                    ArrayList<Integer> arr = checkValidCompress();
                    if (arr != null) {
                        try {
                            model.compressStart(arr);
                            bottomMessage.setText("Compressing: " + inputFile +
                                    "\nQTree: " + model.stringTree(model.getNode()) +
                                    "\nOutput File: " + outputFile +
                                    "\nRaw image size: " + model.getDepth() +
                                    "\nCompressed image size: " + model.getCompressDepth() +
                                    "\nCompression %: " + (((1 - ((double) model.getCompressDepth() / (double) model.getDepth()))) * 100));
                            writeToFileCompressed();
                        } catch (CustomException o) {
                            bottomMessage.setText(o.getMessage());
                        }
                    } else {
                        bottomMessage.setText(errorMessage);
                    }
                });

        MenuItem uncompressButton = new MenuItem("Uncompress");
        uncompressButton.setOnAction(
                e -> {
                    ArrayList<Integer> arr = checkValidUncompress();
                    if (arr != null) {
                        try {
                            model.uncompressStart(arr);
                            bottomMessage.setText("Uncompressing: " + inputFile +
                                    "\nQTree: " + model.stringTree(model.getNode()) +
                                    "\nOutput File: " + outputFile);
                            writeToFileUncompressed();
                        } catch (CustomException o) {
                            bottomMessage.setText(o.getMessage());
                        }
                    } else {
                        bottomMessage.setText(errorMessage);
                    }
                });

        MenuItem viewButton = new MenuItem("View");
        viewButton.setOnAction(
                e -> {
                    ArrayList<Integer> arr = checkValidView();
                    if (arr != null) {
                        try {
                            model.viewStart(arr);
                            bottomMessage.setText("Viewing" + inputFile);
                        } catch (CustomException o) {
                            bottomMessage.setText(o.getMessage());
                        }
                    } else {
                        bottomMessage.setText(errorMessage);
                    }
                });

        MenuItem clearButton = new MenuItem("Clear");
        clearButton.setOnAction(
                e -> {
                    inputLBL.setText("");
                    outputLBL.setText("");
                    inputFile = "";
                    inputFile = "";
                    canvas.setWidth(0);
                    canvas.setHeight(0);
                    bottomMessage.setText("");
                });

        MenuItem quitButton = new MenuItem("Quit");
        quitButton.setOnAction(
                e -> {
                    Platform.exit();
                });

        menuButton.getItems().addAll(compressButton, uncompressButton, viewButton, clearButton, quitButton);

        FlowPane inputRow = new FlowPane();
        inputRow.getChildren().addAll(inputButton, inputLBL);
        inputRow.setPrefWrapLength(800);

        FlowPane outputRow = new FlowPane();
        outputRow.getChildren().addAll(outputButton, outputLBL);
        outputRow.setPrefWrapLength(800);;

        GridPane inputGridPane = new GridPane();

        GridPane.setRowIndex(inputRow, 0);
        GridPane.setRowIndex(outputRow, 1);

        inputGridPane.setHgap(3);
        inputGridPane.setVgap(3);

        group.getChildren().add(canvas);
        GridPane.setRowIndex(group, 2);

        inputGridPane.getChildren().addAll(inputRow, outputRow, group);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        bPane.setTop(menuButton);
        bPane.setCenter(rootGroup);
        bPane.setBottom(bottomMessage);

        stage.setScene(new Scene(bPane));
        stage.setWidth(800);
        stage.setHeight(800);
        stage.show();
    }

    /**
     *
     * error checks the uncompression file and output file for possible issues
     *
     * @return an arraylist null or full
     */
    private ArrayList<Integer> checkValidUncompress() {
        ArrayList<Integer> arr_i = new ArrayList<>();
        try {
            try {
                Scanner in = new Scanner(new FileReader(inputFile));

                FileWriter file = new FileWriter(outputFile);
                file.close();

                while (true) {
                    int temp = Integer.parseInt(in.nextLine());
                    arr_i.add(temp);
                }

            } catch (NullPointerException e ){
                throw new CustomException("File path not present.");
            } catch (FileNotFoundException e) {
                throw new CustomException("File not found.");
            } catch (IOException e) {
                throw new CustomException("File cannot be written to.");
            } catch (NoSuchElementException ignored) {
                return arr_i;
            }
        } catch (CustomException e) {
            errorMessage = e.getMessage();
        }
        return null;
    }

    /**
     *
     * error checks the compression file and output file for possible issues
     *
     * @return an arraylist null or full
     */
    private ArrayList<Integer> checkValidCompress() {
        ArrayList<Integer> arr_i = new ArrayList<>();
        try {
            try {
                Scanner in = new Scanner(new FileReader(inputFile));

                FileWriter file = new FileWriter(outputFile);
                file.close();

                while (true) {
                    int temp = Integer.parseInt(in.nextLine());
                    arr_i.add(temp);
                }

            } catch (NullPointerException e ){
                throw new CustomException("File path not present.");
            } catch (FileNotFoundException e) {
                throw new CustomException("File not found.");
            } catch (IOException e) {
                throw new CustomException("File cannot be written to.");
            } catch (NoSuchElementException ignored) {
                return arr_i;
            }
        } catch (CustomException e) {
            errorMessage = e.getMessage();
        }
        return null;
    }

    /**
     *
     * error checks the view file for possible issues
     *
     * @return an arraylist null or full
     */
    private ArrayList<Integer> checkValidView() {
        ArrayList<Integer> arr_i = new ArrayList<>();
        try {
            try {
                Scanner in = new Scanner(new FileReader(inputFile));
                while (true) {
                    int temp = Integer.parseInt(in.nextLine());
                    if (temp > 255 || temp < 0) {
                        throw new CustomException("Pixel value outside the range 0-255.");
                    }
                    arr_i.add(temp);
                }
            } catch (NullPointerException e ){
                throw new CustomException("File path not present.");
            } catch (FileNotFoundException e) {
                throw new CustomException("File not found.");
            } catch (NumberFormatException e) {
                throw new CustomException("Pixel value not an integer.");
            } catch (NoSuchElementException ignored) {
                if (!model.checkForSquare((int) Math.sqrt(arr_i.size()))) {
                    throw new CustomException("Dimensions not a square.");
                }
                return arr_i;
            }
        } catch (CustomException e) {
            errorMessage = e.getMessage();
        }
        return null;
    }

    /**
     *
     * launches the thread
     *
     * @param args command line args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     *
     * refresshes the gui from nofication
     *
     * @param ritMain model
     */
    private void refresh(RITMain ritMain) {
        canvas.setWidth(model.getDIM());
        canvas.setHeight(model.getDIM());
        int x = 0;
        for (int[] e : ritMain.getImageMatrix()) {
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
     * called when model notifies
     *
     * @param ritMain model
     */
    @Override
    public void update(RITMain ritMain) {
        if (Platform.isFxApplicationThread()) {
            this.refresh(model);
        } else {
            Platform.runLater(() -> this.refresh(model));
        }
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

    /**
     *
     * writes the compressed file
     *
     * @throws CustomException if file is not found
     */
    private void writeToFileCompressed() throws CustomException {
        try {
            FileWriter file = new FileWriter(outputFile);
            file.write(model.getDepth() + "\n");
            writeTree(model.getNode(), file);
            file.close();
        } catch (IOException e) {
            throw new CustomException("File cannot be written to.");
        }
    }

    /**
     *
     * writes the uncompressed file
     *
     * @throws CustomException if file is not found
     */
    private void writeToFileUncompressed() throws CustomException {
        try {
            FileWriter file = new FileWriter(outputFile);
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
            throw new CustomException("Output file cannot be written to.");
        }
    }
}
