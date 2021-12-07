package ch.thegli.example.ziptricks.zip;

public class ZipArchiveLoaderException extends Exception {

    public ZipArchiveLoaderException(String message) {
        super(message);
    }

    public ZipArchiveLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
