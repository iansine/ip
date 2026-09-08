package sine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests command responses exposed to the graphical user interface.
 */
public class SineTest {
    @TempDir
    private Path tempDir;

    /** Tests that GUI commands share state and save changes. */
    @Test
    public void getResponse_addThenList_returnsSavedTask() throws IOException {
        Path dataFile = tempDir.resolve("data/sine.txt");
        Sine sine = new Sine(dataFile.toString());

        String addResponse = sine.getResponse("todo borrow book");
        String listResponse = sine.getResponse("list");

        assertTrue(addResponse.contains("[T][ ] borrow book"));
        assertTrue(listResponse.contains("1.[T][ ] borrow book"));
        assertEquals("T | 0 | borrow book", Files.readString(dataFile).strip());
    }

    /** Tests that validation failures become GUI response text. */
    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Sine sine = new Sine(tempDir.resolve("data/sine.txt").toString());

        String response = sine.getResponse("todo");

        assertEquals("Error :( The description of a todo cannot be empty.", response);
    }

    /** Tests that failed loading still initializes usable state for subsequent commands. */
    @Test
    public void getResponse_malformedSavedData_recoversAndAcceptsCommands() throws IOException {
        Path dataFile = tempDir.resolve("sine.txt");
        Files.writeString(dataFile, "D | 0 | return book | 2026-02-30");
        Sine sine = new Sine(dataFile.toString());

        String firstResponse = sine.getResponse("list");
        String addResponse = sine.getResponse("todo borrow book");

        assertTrue(firstResponse.contains("Starting with an empty list."));
        assertTrue(addResponse.contains("[T][ ] borrow book"));
        assertEquals("T | 0 | borrow book", Files.readString(dataFile).strip());
    }
}
