import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interprets user input and validates command arguments.
 */
public class Parser {
    /**
     * Command categories understood by Sine.
     */
    public enum CommandType {
        BYE, LIST, DELETE, UNMARK, MARK, ADD_TASK, UNKNOWN
    }

    /**
     * Identifies the operation requested by a command.
     *
     * @param command raw user command
     * @return matching command category
     */
    public CommandType getCommandType(String command) {
        if (command.equals("bye")) {
            return CommandType.BYE;
        }
        if (command.equals("list")) {
            return CommandType.LIST;
        }
        if (matches(command, "delete")) {
            return CommandType.DELETE;
        }
        if (matches(command, "unmark")) {
            return CommandType.UNMARK;
        }
        if (matches(command, "mark")) {
            return CommandType.MARK;
        }
        if (matches(command, "todo") || matches(command, "deadline")
                || matches(command, "event")) {
            return CommandType.ADD_TASK;
        }
        return CommandType.UNKNOWN;
    }

    /**
     * Parses a command that does not modify the task list.
     *
     * <p>This transitional method returns {@code null} for mutating commands, which will be
     * extracted into command classes in later increments.</p>
     *
     * @param command raw user command
     * @return exit, list, or unknown command object; null for a mutating command
     */
    public Command parseNonMutatingCommand(String command) {
        CommandType commandType = getCommandType(command);
        switch (commandType) {
        case BYE:
            return new ExitCommand();
        case LIST:
            return new ListCommand();
        case UNKNOWN:
            return new UnknownCommand();
        default:
            return null;
        }
    }

    /**
     * Converts the numeric argument of a task command to a zero-based index.
     *
     * @param command complete delete, mark, or unmark command
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws SineException if the argument is not a valid stored task number
     */
    public int parseTaskIndex(String command, int taskCount) throws SineException {
        int firstSpace = command.indexOf(' ');
        String argument = firstSpace < 0 ? "" : command.substring(firstSpace + 1);
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(argument.trim());
        } catch (NumberFormatException exception) {
            throw new SineException("Please enter a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new SineException("That task number does not exist.");
        }
        return taskNumber - 1;
    }

    /**
     * Creates the task described by an add-task command.
     *
     * @param command todo, deadline, or event command entered by the user
     * @return task represented by the command
     * @throws SineException if a required field is missing or invalid
     */
    public Task parseTask(String command) throws SineException {
        if (matches(command, "todo")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new SineException("The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if (matches(command, "deadline")) {
            String details = command.substring(8).trim();
            int byIndex = details.indexOf("/by");
            String description = byIndex < 0 ? details : details.substring(0, byIndex).trim();
            if (description.isEmpty()) {
                throw new SineException("The description of a deadline cannot be empty.");
            }
            if (byIndex < 0 || details.substring(byIndex + 3).trim().isEmpty()) {
                throw new SineException("The deadline of a deadline cannot be empty.");
            }
            String dateText = details.substring(byIndex + 3).trim();
            try {
                return new Deadline(description, LocalDate.parse(dateText));
            } catch (DateTimeParseException exception) {
                throw new SineException("Please enter the deadline as yyyy-MM-dd.");
            }
        }

        String details = command.substring(5).trim();
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to", Math.max(fromIndex + 5, 0));
        String description = fromIndex < 0 ? details : details.substring(0, fromIndex).trim();
        if (description.isEmpty()) {
            throw new SineException("The description of an event cannot be empty.");
        }
        if (fromIndex < 0 || toIndex < 0
                || details.substring(fromIndex + 5, toIndex).trim().isEmpty()) {
            throw new SineException("The start time of an event cannot be empty.");
        }
        if (details.substring(toIndex + 3).trim().isEmpty()) {
            throw new SineException("The end time of an event cannot be empty.");
        }
        String from = details.substring(fromIndex + 5, toIndex).trim();
        String to = details.substring(toIndex + 3).trim();
        return new Event(description, from, to);
    }

    /**
     * Checks whether input is a command word, optionally followed by arguments.
     *
     * @param input raw user input
     * @param commandWord command word to match
     * @return true if the input starts with the complete command word
     */
    private boolean matches(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
    }
}
