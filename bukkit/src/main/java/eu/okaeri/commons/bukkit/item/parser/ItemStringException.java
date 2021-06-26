package eu.okaeri.commons.bukkit.item.parser;

public class ItemStringException extends RuntimeException {

    public ItemStringException() {
    }

    public ItemStringException(String message) {
        super(message);
    }

    public ItemStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItemStringException(Throwable cause) {
        super(cause);
    }

    public ItemStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
