package ch.thegli.example.ziptricks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    private static LogHandler handler = new LogHandler();

    /*
     * set up special log handler for test assertions
     */
    @BeforeAll
    static void initLogging() {
        final Level logLevel = Level.INFO;
        final Logger rootLogger = LogManager.getLogManager().getLogger("");
        handler.setLevel(Level.ALL);
        rootLogger.addHandler(handler);
        rootLogger.setLevel(logLevel);
        // silence the normal handlers
        rootLogger.setUseParentHandlers(false);
    }

    @BeforeEach
    void resetLogHandler() {
        handler.resetLastRecord();
    }

    @Test
    void validZipFileArg() {
        Main.main(new String[]{"src/test/resources/flat.zip"});

        assertTrue(handler.getLastRecord().getMessage().startsWith("Total bytes loaded: "));
    }

    @Test
    void missingFileArg() {
        Main.main(new String[]{"this-file-is-not-there"});

        assertTrue(handler.getLastRecord().getMessage().startsWith("A fatal error occurred."));
    }

    @Test
    void missingArg() {
        (new Main()).main(new String[0]);

        assertTrue(handler.getLastRecord().getMessage().startsWith("No zip file path specified."));
    }

    private static final class LogHandler extends Handler {
        private LogRecord lastRecord = null;

        public LogRecord getLastRecord() {
            return lastRecord;
        }

        public void resetLastRecord() {
            lastRecord = null;
        }

        public void publish(LogRecord record) {
            lastRecord = record;
        }

        public void close() {
        }

        public void flush() {
        }
    }

}
