package sine.command;

import sine.storage.Storage;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Ends the chatbot session after showing the farewell message.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
