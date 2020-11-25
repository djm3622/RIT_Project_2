package model;

// imports
import java.util.ArrayList;
import java.util.List;

/**
 * model to create the image matrix to display
 *
 * @author David Millard
 */
public class RITMain {
    /** the observers of this model */
    private List<Observer<RITMain>> observers;

    private ArrayList<Integer> arr_i;
    private int DIM;
    private int[][] imageMatrix;
    private int depth;
    private RITQTNode node;
    private int compressDepth = 1;

    /**
     * run the selected options
     */
    public RITMain(ArrayList<Integer> arr_i, int selection) {
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
            case (2) -> {
                this.depth = arr_i.size();
                this.DIM = (int) Math.sqrt(arr_i.size());
                this.imageMatrix = new int[(int) this.DIM][(int) this.DIM];
                this.arr_i = arr_i;
                createImageMatrix();
                this.node = compress(this.DIM, 0, 0);
            }
        }
    }

    /**
     *
     * the getter for dimesions
     *
     * @return the dimensions
     */
    public int getDIM() {
        return this.DIM;
    }

    /**
     *
     * getter for image matrix
     *
     * @return the image matrix
     */
    public int[][] getImageMatrix() {
        return imageMatrix;
    }

    /**
     *
     * getter for node
     *
     * @return the rit tree
     */
    public RITQTNode getNode() {
        return this.node;
    }

    /**
     *
     * getter for array size
     *
     * @return array size
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     *
     * getter for tree size
     *
     * @return tree size
     */
    public int getCompressDepth() {
        return this.compressDepth;
    }

    /**
     *
     * creates the image matrix using pixel data
     *
     */
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

    /**
     *
     * polymorphised image matrix using an rit tree
     *
     * @param node rit tree
     * @param row row
     * @param col column
     * @param dim dimesions
     */
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

    /**
     *
     * builds the parts of matrix physically in the matrix
     * used with the rit tree matrix creation
     *
     * @param row row
     * @param col column
     * @param dim dimensions
     * @param val value to write
     */
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

    /**
     *
     * parses the list of ints to create the rit tree
     *
     * @param arr list of ints
     * @return rit tree
     */
    public RITQTNode parse(List<Integer> arr) {
        int arr_e = arr.remove(0);
        if (arr_e == -1) {
            return new RITQTNode(arr_e, parse(arr), parse(arr), parse(arr), parse(arr));
        } else {
            return new RITQTNode(arr_e);
        }
    }

    /**
     *
     * compress an uncompresses array to an ritqnode tree
     *
     * @param dim dim of matrix to recursively check parts
     * @param row starting row
     * @param col starting col
     * @return an rit q node tree
     */
    public RITQTNode compress(int dim, int row, int col) {
        if (checkMatrix(dim, row, col)) {
            this.compressDepth++;
            return new RITQTNode(this.imageMatrix[col][row]);
        } else {
            this.compressDepth++;
            return new RITQTNode(-1, compress(dim/2, row, col),
                                         compress(dim/2, row, col  + (dim / 2)),
                                         compress(dim/2, row + (dim / 2), col),
                                         compress(dim/2, row + (dim / 2), col  + (dim / 2)));
        }
    }

    /**
     *
     * check that the parts of matrix are the same
     *
     * @param dim size of part of matrix
     * @param row starting row
     * @param col starting col
     * @return whether the matrix is all one value
     */
    private boolean checkMatrix(int dim, int row, int col) {
        boolean check = true;
        int ref = this.imageMatrix[col][row];
        for (int x = col; x < dim+col; x++) {
            for (int y = row; y < dim+row; y++) {
                if (this.imageMatrix[x][y] != ref) {
                    check = false;
                    break;
                }
            }
            if (!check) {
                break;
            }
        }
        return check;
    }

    /**
     *
     * preorder traversal of rit tree
     *
     * @param node rit tree
     */
    public void printTree(model.RITQTNode node) {
        if (node != null) {
            System.out.print(node + " ");
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case (0) -> printTree(node.getUpperLeft());
                    case (1) -> printTree(node.getUpperRight());
                    case (2) -> printTree(node.getLowerLeft());
                    case (3) -> printTree(node.getLowerRight());
                }
            }
        }
    }
}
