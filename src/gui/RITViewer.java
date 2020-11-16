package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.Observer;
import model.RITMain;
import model.RITQTNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RITViewer extends Application implements Observer<RITMain> {
    private List<Integer> arr_i;
    private static String[] arg;
    private RITMain model;
    private RITQTNode image;

    /**
     *
     *
     *
     */
    @Override
    public void init() {
        arr_i = new ArrayList<>();

        try {
            Scanner in = new Scanner(new FileReader(arg[0]));
            while (true) {
                arr_i.add(Integer.parseInt(in.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (NoSuchElementException e) {
            System.out.print("");
        }
        model = new RITMain();
    }

    /**
     *
     *
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {



        stage.show();
    }

    /**
     *
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        arg = args;
        Application.launch(args);
    }


    private static ArrayList<Integer> convertArray(ArrayList<String> arr) {
        ArrayList<Integer> arr_i = new ArrayList<>(arr.size());
        for (String e: arr) {
            arr_i.add(Integer.parseInt(e));
        }
        return arr_i;
    }

    /**
     *
     *
     *
     * @param ritMain
     */
    private void refresh(RITMain ritMain) {
        //ritMain.parse()
    }

    /**
     *
     *
     *
     * @param ritMain
     */
    @Override
    public void update(RITMain ritMain) {
        if (Platform.isFxApplicationThread()) {
            this.refresh(ritMain);
        } else {
            Platform.runLater(() -> this.refresh(ritMain));
        }
    }
}
