package sine.command;

import java.io.IOException;

import sine.storage.Storage;
import sine.task.Task;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Deletes one task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command that deletes the task at a zero-based index.
     *
     * @param taskIndex zero-based index of the task to delete
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        Task removedTask = tasks.delete(taskIndex);
        storage.save(tasks.getTasks());
        ui.showDeletedTask(removedTask, tasks.size());
    }
}
