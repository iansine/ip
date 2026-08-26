package sine.command;

import java.io.IOException;

import sine.storage.Storage;
import sine.task.Task;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Adds one parsed task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task task parsed from the user's command
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAddedTask(task, tasks.size());
    }
}
