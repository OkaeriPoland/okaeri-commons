package eu.okaeri.commons.classpath;

public class ClasspathScannerException extends RuntimeException {

    public ClasspathScannerException() {
    }

    public ClasspathScannerException(String message) {
        super(message);
    }

    public ClasspathScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClasspathScannerException(Throwable cause) {
        super(cause);
    }

    public ClasspathScannerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
