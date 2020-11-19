package model;

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
