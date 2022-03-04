package ch.thegli.example.ziptricks;

import ch.thegli.example.ziptricks.zip.ZipArchiveLoader;
import ch.thegli.example.ziptricks.zip.ZipArchiveLoaderException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class to demonstrate the use of method <code>ZipArchiveLoader.load(String)</code>.
 */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                final String zipFilePath = args[0];
                final List<byte[]> fileContents = ZipArchiveLoader.getInstance().load(zipFilePath);
                LOGGER.log(Level.INFO, "Loaded contents from file [{0}]: {1}", new Object[]{zipFilePath, fileContents.size()});

                long totalBytes = 0;
                for (int i = 0; i < fileContents.size(); i++) {
                    int len = fileContents.get(i).length;
                    LOGGER.log(Level.INFO, "Content[{0}] size={1}", new Object[]{i, len});
                    totalBytes += len;
                }
                LOGGER.log(Level.INFO, "Total bytes loaded: {0}", totalBytes);
            } catch (ZipArchiveLoaderException e) {
                LOGGER.severe("A fatal error occurred.");
            }
        } else {
            LOGGER.warning("No zip file path specified.");
        }
    }
}
