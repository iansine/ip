package sine.command;

import sine.storage.Storage;
import sine.task.TaskList;
import sine.ui.Ui;

/**
 * Responds to input that does not match a supported command.
 */
public class UnknownCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showUnknownCommand();
    }
}
