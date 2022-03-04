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
    private static ZipArchiveLoader instance;

    private ZipArchiveLoader() {
    }

    public static ZipArchiveLoader getInstance() {
        if (instance == null) {
            instance = new ZipArchiveLoader();
        }

        return instance;
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
                    LOGGER.log(Level.FINEST, "Next file from ZIP archive to load: [{0}], size={1}", new Object[]{name, size});

                    int len;
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        while ((len = zis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }

                        final byte[] fileBytes = out.toByteArray();
                        LOGGER.log(Level.FINEST, "Loaded file [{0}], bytes read={1}", new Object[]{name, fileBytes.length});

                        if (size != fileBytes.length) {
                            LOGGER.log(Level.WARNING, "Mismatch between read bytes ({0}) and expected file length ({1})!", new Object[]{fileBytes.length, size});
                        }

                        fileContents.add(fileBytes);
                    }
                }

                entry = zis.getNextEntry();
            }
            LOGGER.log(Level.FINE, "Total loaded files from ZIP archive [{0}]: {1}", new Object[]{zipFile.getAbsolutePath(), fileContents.size()});
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

        LOGGER.log(Level.FINEST, "Created handle for file [{0}]", zipFile.getAbsolutePath());
        return zipFile;
    }
}
