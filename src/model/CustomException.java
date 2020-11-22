package model;

/**
 * a custom exception to display the error message input
 *
 * @author David Millard
 */
public class CustomException extends Exception {

    // private variables
    private final String msg;

    /**
     *
     * construcor for custom exception
     *
     * @param msg message to display
     */
    public CustomException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
