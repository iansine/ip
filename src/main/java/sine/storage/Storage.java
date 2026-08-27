package sine.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import sine.task.Deadline;
import sine.task.Event;
import sine.task.Task;
import sine.task.Todo;

/**
 * Loads and saves tasks using Sine's text-file format.
 */
public class Storage {
    private final Path dataFile;

    /**
     * Creates storage that uses the given OS-independent relative file path.
     *
     * @param filePath Path to the task data file.
     */
    public Storage(String filePath) {
        this.dataFile = Path.of(filePath);
    }

    /**
     * Loads tasks from the data file, or returns an empty list if it does not exist yet.
     *
     * @return Tasks stored during the previous run.
     * @throws IOException If the existing data file cannot be read or contains invalid data.
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(dataFile)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(dataFile);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (!line.isBlank()) {
                tasks.add(parseStoredTask(line, i + 1));
            }
        }
        return tasks;
    }

    /**
     * Rewrites the data file with the current task list.
     *
     * @param tasks Tasks to save.
     * @throws IOException If the data directory or file cannot be written.
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(dataFile.getParent());
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            String status = task.isDone() ? "1" : "0";
            if (task instanceof Deadline deadline) {
                lines.add("D | " + status + " | " + encodeField(task.getDescription())
                        + " | " + deadline.getBy());
            } else if (task instanceof Event event) {
                lines.add("E | " + status + " | " + encodeField(task.getDescription())
                        + " | " + encodeField(event.getFrom())
                        + " | " + encodeField(event.getTo()));
            } else {
                lines.add("T | " + status + " | " + encodeField(task.getDescription()));
            }
        }
        Files.write(dataFile, lines);
    }

    /**
     * Converts one validated storage record into a task.
     *
     * @param line Storage record to parse.
     * @param lineNumber One-based line number used in error messages.
     * @return Task represented by the record.
     * @throws IOException If the record contains invalid task data.
     */
    private Task parseStoredTask(String line, int lineNumber) throws IOException {
        List<String> fields = splitStoredFields(line, lineNumber);
        if (fields.size() < 2
                || (!fields.get(1).equals("0") && !fields.get(1).equals("1"))) {
            throw invalidData(lineNumber);
        }

        int expectedFieldCount;
        switch (fields.get(0)) {
            case "T":
                expectedFieldCount = 3;
                break;
            case "D":
                expectedFieldCount = 4;
                break;
            case "E":
                expectedFieldCount = 5;
                break;
            default:
                throw invalidData(lineNumber);
        }
        if (fields.size() != expectedFieldCount) {
            throw invalidData(lineNumber);
        }
        for (int i = 2; i < fields.size(); i++) {
            if (fields.get(i).isBlank()) {
                throw invalidData(lineNumber);
            }
        }

        Task task;
        switch (fields.get(0)) {
            case "T":
                task = new Todo(fields.get(2));
                break;
            case "D":
                try {
                    task = new Deadline(fields.get(2), LocalDate.parse(fields.get(3)));
                } catch (DateTimeParseException exception) {
                    throw invalidData(lineNumber);
                }
                break;
            case "E":
                task = new Event(fields.get(2), fields.get(3), fields.get(4));
                break;
            default:
                throw new AssertionError("Task type was validated above");
        }
        if (fields.get(1).equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a storage record while decoding escaped pipe and backslash characters.
     *
     * @param line Storage record to split.
     * @param lineNumber One-based line number used in error messages.
     * @return Decoded fields from the record.
     * @throws IOException If the record contains an invalid escape sequence.
     */
    private List<String> splitStoredFields(String line, int lineNumber) throws IOException {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\\') {
                if (i + 1 >= line.length()) {
                    throw invalidData(lineNumber);
                }
                char escapedCharacter = line.charAt(++i);
                if (escapedCharacter != '\\' && escapedCharacter != '|') {
                    throw invalidData(lineNumber);
                }
                field.append(escapedCharacter);
            } else if (line.startsWith(" | ", i)) {
                fields.add(field.toString());
                field.setLength(0);
                i += 2;
            } else {
                field.append(line.charAt(i));
            }
        }
        fields.add(field.toString());
        return fields;
    }

    /**
     * Creates a consistent error for a malformed line in the data file.
     *
     * @param lineNumber One-based line number containing invalid data.
     * @return Error describing the malformed record.
     */
    private IOException invalidData(int lineNumber) {
        return new IOException("Invalid task data on line " + lineNumber);
    }

    /**
     * Escapes characters that have a special meaning in the storage format.
     *
     * @param field Task text to encode.
     * @return Text safe to store as one field.
     */
    private String encodeField(String field) {
        return field.replace("\\", "\\\\").replace("|", "\\|");
    }
}
