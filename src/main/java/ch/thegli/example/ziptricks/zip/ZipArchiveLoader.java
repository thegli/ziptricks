package ch.thegli.example.ziptricks.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipArchiveLoader {
    private static final Logger LOGGER = Logger.getLogger(ZipArchiveLoader.class.getName());
    private static ZipArchiveLoader INSTANCE;

    private ZipArchiveLoader() {
    }

    public static ZipArchiveLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZipArchiveLoader();
        }

        return INSTANCE;
    }

    public List<byte[]> load(String zipFilePath) throws ZipArchiveLoaderException {
        final File zipFile = getFile(zipFilePath);
        List<byte[]> fileContents = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zis.getNextEntry();
            byte[] buffer = new byte[4096];

            while (entry != null) {
                if (!entry.isDirectory()) {
                    String name = entry.getName();
                    long size = entry.getSize();
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("Next file from ZIP archive to load: [" + name + "], size=" + size);
                    }

                    int len;
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        while ((len = zis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }

                        final byte[] fileBytes = out.toByteArray();
                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest("Loaded file [" + name + "], bytes read=" + fileBytes.length);
                        }

                        if (size != fileBytes.length) {
                            LOGGER.warning("Mismatch between read bytes (" + fileBytes.length + ") and expected file length (" + size + ")!");
                        }

                        fileContents.add(fileBytes);
                    }
                }

                entry = zis.getNextEntry();
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Total loaded files from ZIP archive [" + zipFile.getAbsolutePath() + "]: " + fileContents.size());
            }
        } catch (IOException ioe) {
            String errorMsg = "Failed to load content from zip file due to IOException: " + ioe.getMessage();
            LOGGER.severe(errorMsg);
            throw new ZipArchiveLoaderException(errorMsg, ioe);
        }

        return fileContents;
    }

    private File getFile(String zipFilePath) throws ZipArchiveLoaderException {
        if (zipFilePath == null) {
            String errorMsg = "No zip file path given!";
            LOGGER.severe(errorMsg);
            throw new ZipArchiveLoaderException(errorMsg);
        }

        final File zipFile = new File(zipFilePath);
        if (!zipFile.isFile()) {
            String errorMsg = "Given file path [" + zipFilePath + "] does not denote a readable file!";
            LOGGER.severe(errorMsg);
            throw new ZipArchiveLoaderException(errorMsg);
        }

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("Created handle for file [" + zipFile.getAbsolutePath() + "]");
        }
        return zipFile;
    }
}
