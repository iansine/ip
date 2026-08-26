package sine.command;

import sine.storage.Storage;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Displays all tasks currently stored in the task list.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
