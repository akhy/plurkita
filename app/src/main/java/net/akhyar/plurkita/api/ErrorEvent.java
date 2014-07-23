package net.akhyar.plurkita.api;

/**
 * @author akhyar
 */
public class ErrorEvent {
    private Throwable throwable;
    private String message;

    public ErrorEvent(Throwable throwable) {
        this.throwable = throwable;
        this.message = throwable.getMessage();
    }

    public ErrorEvent(Throwable throwable, String message) {
        this.throwable = throwable;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
