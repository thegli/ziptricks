package ch.thegli.example.ziptricks;

import ch.thegli.example.ziptricks.zip.ZipArchiveLoader;
import ch.thegli.example.ziptricks.zip.ZipArchiveLoaderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZipArchiveLoaderTest {

    /*
     * set log level on root logger and its handlers
     */
    @BeforeAll
    static void initLogging() {
        final Level logLevel = Level.FINEST;
        final Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(logLevel);
        Arrays.stream(rootLogger.getHandlers()).forEach(h -> h.setLevel(logLevel));
    }

    @Test
    void validZipFile() throws ZipArchiveLoaderException {
        String validZipFile = getTestFilePath("flat.zip");

        List<byte[]> contents = ZipArchiveLoader.getInstance().load(validZipFile);

        assertEquals(4, contents.size());
        assertContents(contents);
    }

    @Test
    void validZipFileWithDirectories() throws ZipArchiveLoaderException {
        String withDirectoriesZipFile = getTestFilePath("directories.zip");

        List<byte[]> contents = ZipArchiveLoader.getInstance().load(withDirectoriesZipFile);

        assertEquals(3, contents.size());
        assertContents(contents);
    }

    @Test
    void validBiggerZipFile() throws ZipArchiveLoaderException {
        String validBigZipFile = getTestFilePath("bigger.zip");

        List<byte[]> contents = ZipArchiveLoader.getInstance().load(validBigZipFile);

        assertEquals(6, contents.size());
        assertContents(contents);
    }

    @Test
    void fileIsNoZip() throws ZipArchiveLoaderException {
        String noZipFile = getTestFilePath("no-zip-file");

        List<byte[]> contents = ZipArchiveLoader.getInstance().load(noZipFile);

        assertTrue(contents.isEmpty());
    }

    @Test
    void corruptZipFile() {
        String corruptZipFile = getTestFilePath("corrupt.zip");

        assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(corruptZipFile));
    }

    @Test
    void fileIsDirectory() {
        String directoryFile = getTestFilePath("");

        assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(directoryFile));
    }

    @Test
    void fileDoesNotExist() {
        String unknownFile = getTestFilePath("this-file-does-not-exist");

        assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(unknownFile));
    }

    private String getTestFilePath(String filename) {
        return "src/test/resources/" + filename;
    }

    private void assertContents(List<byte[]> contents) {
        for (byte[] content : contents) {
            assertNotNull(content);
            assertTrue(content.length > 0);
        }
    }
}
