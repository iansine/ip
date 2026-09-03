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
}
