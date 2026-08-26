package sine.command;

import java.io.IOException;

import sine.storage.Storage;
import sine.task.Task;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Marks one task as completed.
 */
public class MarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command that marks the task at a zero-based index.
     *
     * @param taskIndex zero-based index of the task to mark
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        storage.save(tasks.getTasks());
        ui.showMarkedTask(task);
    }
}
