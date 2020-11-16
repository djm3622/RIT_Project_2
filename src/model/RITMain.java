package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class RITMain {

    /** the observers of this model */
    private final List<Observer<RITMain>> observers;
    private final ArrayList<Integer> arr_i;
    private final double DIM;
    private int[][] imageMatrix;

    /**
     * Create a new board.
     */
    public RITMain(ArrayList<Integer> arr_i) {
        this.observers = new LinkedList<>();
        this.arr_i = arr_i;
        this.DIM = Math.sqrt(arr_i.size());
        this.imageMatrix = new int[(int) this.DIM][(int) this.DIM];
    }

    public double getDIM() {
        return this.DIM;
    }

    private void createImageMatrix() {

        // use the matrix created here to display on the viewer

        int y = 0;
        int x = 0;
        for (int e: this.arr_i) {
            if (x == this.DIM) {
                y++;
                x = 0;
            }
            imageMatrix[x][y] = e;
            x++;
        }
    }

    /**
     * The view calls this method to add themselves as an observer of the model.
     *
     * @param observer the observer
     */
    public void addObserver(Observer<RITMain> observer) {
        this.observers.add(observer);
    }

    /** When the model changes, the observers are notified via their update() method */
    private void notifyObservers() {
        for (Observer<RITMain> obs: this.observers ) {
            obs.update(this);
        }
    }


    public RITQTNode parse(List<Integer> arr) {
        int arr_e = arr.remove(0);
        if (arr_e == -1) {
            return new RITQTNode(arr_e, parse(arr), parse(arr), parse(arr), parse(arr));
        } else {
            return new RITQTNode(arr_e);
        }
    }
}
