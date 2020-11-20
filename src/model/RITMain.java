package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RITMain {
    /** the observers of this model */
    private List<Observer<RITMain>> observers;

    private ArrayList<Integer> arr_i;
    private int DIM;
    private int[][] imageMatrix;
    private final int selection;
    private int depth;
    private RITQTNode node;
    private ArrayList<ArrayList<Integer>> arrayList;

    /**
     * Create a new board.
     */
    public RITMain(ArrayList<Integer> arr_i, int selection) {
        this.selection = selection;
        switch (selection) {
            case (0) -> {
                this.arr_i = arr_i;
                this.DIM = (int) Math.sqrt(arr_i.size());
                this.imageMatrix = new int[(int) this.DIM][(int) this.DIM];
            }
            case (1) -> {
                this.depth = arr_i.remove(0);
                this.DIM = (int) Math.sqrt(this.depth);
                this.imageMatrix = new int[(int) this.DIM][(int) this.DIM];
                this.node = parse(arr_i);
                createImageMatrix(this.node, 0, 0, this.DIM);
            }
        }
    }

    public int getDIM() {
        return this.DIM;
    }

    public int[][] getImageMatrix() {
        return imageMatrix;
    }

    public void createImageMatrix() {
        int y = 0;
        int x = 0;
        for (int e : this.arr_i) {
            if (x == this.DIM) {
                y++;
                x = 0;
            }
            imageMatrix[x][y] = e;
            x++;
        }
    }


    public void createImageMatrix(RITQTNode node, int row, int col, int dim) {
        RITQTNode temp_ul = node.getUpperLeft();
        if (temp_ul.getVal() == -1) {
            createImageMatrix(temp_ul, row, col,dim / 2);
        } else {
            buildImagineMatrix(row, col, dim / 2, temp_ul.getVal());
        }

        RITQTNode temp_ur = node.getUpperRight();
        if (temp_ur.getVal() == -1) {
            createImageMatrix(temp_ur, row, col + (dim / 2),dim / 2);
        } else {
            buildImagineMatrix(row, col + (dim / 2), dim / 2, temp_ur.getVal());
        }

        RITQTNode temp_ll = node.getLowerLeft();
        if (temp_ll.getVal() == -1) {
            createImageMatrix(temp_ll, row + (dim / 2), col,dim / 2);
        } else {
            buildImagineMatrix(row + (dim / 2), col, dim / 2, temp_ll.getVal());
        }

        RITQTNode temp_lr = node.getLowerRight();
        if (temp_lr.getVal() == -1) {
            createImageMatrix(temp_lr, row + (dim / 2), col + (dim / 2),dim / 2);
        } else {
            buildImagineMatrix(row + (dim / 2), col  + (dim / 2), dim / 2, temp_lr.getVal());
        }
    }

    private void buildImagineMatrix(int row, int col, int dim, int val) {
        for (int x = col; x < dim+col; x++) {
            for (int y = row; y < dim+row; y++) {
                this.imageMatrix[x][y] = val;
            }
        }
    }

    /**
     *
     * check for the dimensions
     *
     * @param num the dimesnions
     * @return whether it is a square or not
     */
    public boolean checkForSquare(int num) {
        int check = 1;
        while (true) {
            if (num == check) {
                return true;
            } else if (num < check) {
                return false;
            } else {
                check *= 2;
            }
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
