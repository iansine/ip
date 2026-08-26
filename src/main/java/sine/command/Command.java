package sine.command;

import java.io.IOException;

import sine.storage.Storage;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Represents an instruction that can be executed by the chatbot.
 */
public abstract class Command {
    /**
     * Executes this command using the application's shared components.
     *
     * @param tasks current task list
     * @param ui console user interface
     * @param storage task persistence service
     * @throws IOException if the command cannot save task changes
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws IOException;

    /**
     * Indicates whether executing this command should end the application.
     *
     * @return true only for an exit command
     */
    public boolean isExit() {
        return false;
    }
}
