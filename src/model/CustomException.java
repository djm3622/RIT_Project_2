package model;

/**
 * a custom exception to display the error message input
 *
 * @author David Millard
 */
public class CustomException extends Exception {

    private final String msg;

    public CustomException(String msg) {
        super(msg);
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
