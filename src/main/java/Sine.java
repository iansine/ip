import java.io.IOException;

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
     * @param filePath relative path to the task data file
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
                Command parsedCommand = parser.parseNonMutatingCommand(command);
                if (parsedCommand != null) {
                    parsedCommand.execute(tasks, ui, storage);
                    if (parsedCommand.isExit()) {
                        break;
                    }
                    continue;
                }

                Parser.CommandType commandType = parser.getCommandType(command);
                if (commandType == Parser.CommandType.DELETE) {
                    int taskIndex = parser.parseTaskIndex(command, tasks.size());
                    Task removedTask = tasks.delete(taskIndex);
                    storage.save(tasks.getTasks());
                    ui.showDeletedTask(removedTask, tasks.size());
                    continue;
                }

                if (commandType == Parser.CommandType.UNMARK) {
                    int taskIndex = parser.parseTaskIndex(command, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks.getTasks());
                    ui.showUnmarkedTask(tasks.get(taskIndex));
                    continue;
                }

                if (commandType == Parser.CommandType.MARK) {
                    int taskIndex = parser.parseTaskIndex(command, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks.getTasks());
                    ui.showMarkedTask(tasks.get(taskIndex));
                    continue;
                }

                if (commandType == Parser.CommandType.ADD_TASK) {
                    Task newTask = parser.parseTask(command);
                    tasks.add(newTask);
                    storage.save(tasks.getTasks());
                    ui.showAddedTask(newTask, tasks.size());
                    continue;
                }

            } catch (SineException exception) {
                ui.showCommandError(exception.getMessage());
            } catch (IOException exception) {
                ui.showSavingError();
            }
        }
    }

    public static void main(String[] args) {
        new Sine("data/sine.txt").run();
    }
}
