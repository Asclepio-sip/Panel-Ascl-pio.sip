package Asclepio.exception;

public class ApiExternaException extends RuntimeException {

    private final int status;

    public ApiExternaException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}