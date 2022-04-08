package ch.thegli.example.ziptricks.zip;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

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

        ZipArchiveLoaderException e = assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(corruptZipFile));
        assertTrue(e.getMessage().startsWith("Failed to load content from zip file due to IOException:"));
    }

    @Test
    void fileIsDirectory() {
        String directoryFile = getTestFilePath("");

        ZipArchiveLoaderException e = assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(directoryFile));
        assertEquals("Given file path [" + directoryFile + "] does not denote a readable file!", e.getMessage());
    }

    @Test
    void fileDoesNotExist() {
        String unknownFile = getTestFilePath("this-file-does-not-exist");

        ZipArchiveLoaderException e = assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(unknownFile));
        assertEquals("Given file path [" + unknownFile + "] does not denote a readable file!", e.getMessage());
    }

    @Test
    void nullFile() {
        ZipArchiveLoaderException e = assertThrows(ZipArchiveLoaderException.class,
                () -> ZipArchiveLoader.getInstance().load(null));
        assertEquals("No zip file path given!", e.getMessage());
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
