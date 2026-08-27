package sine;

import java.io.IOException;

import sine.command.Command;
import sine.exception.SineException;
import sine.parser.Parser;
import sine.storage.Storage;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Starts the Sine chatbot application.
 */
public class Sine {
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    /**
     * Creates a chatbot that stores its tasks at the given relative file path.
     *
     * @param filePath Relative path to the task data file.
     */
    public Sine(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = new TaskList();
    }

    /**
     * Loads saved tasks and processes commands until input ends or the user exits.
     */
    public void run() {
        ui.showWelcome();
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            tasks = new TaskList();
            ui.showLoadingError();
        }
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            try {
                Command parsedCommand = parser.parse(command, tasks.size());
                parsedCommand.execute(tasks, ui, storage);
                if (parsedCommand.isExit()) {
                    break;
                }
            } catch (SineException exception) {
                ui.showCommandError(exception.getMessage());
            } catch (IOException exception) {
                ui.showSavingError();
            }
        }
    }

    /**
     * Starts Sine using the default relative data-file path.
     *
     * @param args Command-line arguments, which Sine does not use.
     */
    public static void main(String[] args) {
        new Sine("data/sine.txt").run();
    }
}
