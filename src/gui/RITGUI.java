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


public class RITGUI extends Application implements Observer<RITMain> {

    private RITMain model;
    private String inputFile;
    private String outputFile;
    private int selection;
    private String errorMessage;
    GraphicsContext gc;
    Canvas canvas;

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
                    this.selection = 1;
                    ArrayList<Integer> arr = checkValidCompress();
                    if (arr != null) {
                        model.compressStart(arr);
                    } else {
                        System.out.println(errorMessage);
                    }
                });

        MenuItem uncompressButton = new MenuItem("Uncompress");
        uncompressButton.setOnAction(
                e -> {
                    this.selection = 2;
                    ArrayList<Integer> arr = checkValidUncompress();
                    if (arr != null) {
                        model.uncompressStart(arr);
                    } else {
                        System.out.println(errorMessage);
                    }
                });

        MenuItem viewButton = new MenuItem("View");
        viewButton.setOnAction(
                e -> {
                    this.selection = 0;
                    ArrayList<Integer> arr = checkValidView();
                    if (arr != null) {
                        model.viewStart(arr);
                    } else {
                        System.out.println(errorMessage);
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
                });

        MenuItem quitButton = new MenuItem("Quit");
        quitButton.setOnAction(
                e -> {
                    this.selection = 4;
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

        stage.setScene(new Scene(bPane));
        stage.setWidth(800);
        stage.setHeight(800);
        stage.show();
    }

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

    public static void main(String[] args) {
        Application.launch(args);
    }

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

    @Override
    public void update(RITMain ritMain) {
        if (Platform.isFxApplicationThread()) {
            this.refresh(model);
        } else {
            Platform.runLater(() -> this.refresh(model));
        }
    }
}
