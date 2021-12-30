package ch.thegli.example.ziptricks;

import ch.thegli.example.ziptricks.zip.ZipArchiveLoader;
import ch.thegli.example.ziptricks.zip.ZipArchiveLoaderException;

import java.util.List;

/**
 * Main class to demonstrate the use of method <code>ZipArchiveLoader.load(String)</code>.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                final String zipFilePath = args[0];
                final List<byte[]> fileContents = ZipArchiveLoader.getInstance().load(zipFilePath);
                System.out.println("Loaded contents from file [" + zipFilePath + "]: " + fileContents.size());
                long totalBytes = 0;
                for (int i = 0; i < fileContents.size(); i++) {
                    int len = fileContents.get(i).length;
                    System.out.println("Content[" + i + "] size=" + len);
                    totalBytes += len;
                }
                System.out.println("Total bytes loaded: " + totalBytes);
            } catch (ZipArchiveLoaderException zae) {
                zae.printStackTrace();
            }
        } else {
            System.err.println("No zip file path specified.");
        }
    }
}
