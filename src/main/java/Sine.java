import java.io.IOException;

/**
 * Starts the Sine chatbot application.
 */
public class Sine {
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("data/sine.txt");
        Parser parser = new Parser();
        ui.showWelcome();

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            tasks = new TaskList();
            ui.showLoadingError();
        }
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();

            try {
                Parser.CommandType commandType = parser.getCommandType(command);
                if (commandType == Parser.CommandType.BYE) {
                    ui.showGoodbye();
                    break;
                }

                if (commandType == Parser.CommandType.LIST) {
                    ui.showTaskList(tasks);
                    continue;
                }

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

                ui.showUnknownCommand();
            } catch (SineException exception) {
                ui.showCommandError(exception.getMessage());
            } catch (IOException exception) {
                ui.showSavingError();
            }
        }
    }

}
