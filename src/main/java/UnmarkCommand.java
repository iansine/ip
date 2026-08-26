import java.io.IOException;

/**
 * Marks one task as not completed.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command that unmarks the task at a zero-based index.
     *
     * @param taskIndex zero-based index of the task to unmark
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        Task task = tasks.get(taskIndex);
        task.markAsNotDone();
        storage.save(tasks.getTasks());
        ui.showUnmarkedTask(task);
    }
}
