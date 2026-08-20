import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Starts the Sine chatbot application.
 */
public class Sine {
    private static final String SEPARATOR =
            "____________________________________________________________";

    public static void main(String[] args) {
        String banner = " ____  _            \n"
                + "/ ___|(_)_ __   ___ \n"
                + "\\___ \\| | '_ \\ / _ \\\n"
                + " ___) | | | | |  __/\n"
                + "|____/|_|_| |_|\\___|";

        System.out.println(SEPARATOR);
        System.out.println(banner);
        System.out.println("Hello! I'm Sine.");
        System.out.println("What's up?");
        System.out.println(SEPARATOR);

        List<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            try {
                if (command.equals("bye")) {
                    System.out.println(" Bye. I'll be here if you need me :)");
                    System.out.println(SEPARATOR);
                    break;
                }

                if (command.equals("list")) {
                    System.out.println(" TODO list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println(SEPARATOR);
                    continue;
                }

                if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(command.substring(6), tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println(" Roger that. I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskIndex));
                    System.out.println(SEPARATOR);
                    continue;
                }

                if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(command.substring(4), tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println(" Great work! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskIndex));
                    System.out.println(SEPARATOR);
                    continue;
                }

                Task newTask = createTask(command);
                if (newTask != null) {
                    tasks.add(newTask);
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(SEPARATOR);
                } else {
                    System.out.println(" Must have been the wind");
                    System.out.println(SEPARATOR);
                }
            } catch (SineException exception) {
                System.out.println(" Error :( " + exception.getMessage());
                System.out.println(SEPARATOR);
            }
        }
    }

    /**
     * Converts a user-supplied task number to its zero-based array index.
     *
     * @param argument text following the mark or unmark command
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws SineException if the argument is not a valid stored task number
     */
    private static int parseTaskIndex(String argument, int taskCount) throws SineException {
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
     * Creates the task described by a command after validating its required fields.
     *
     * @param command command entered by the user
     * @return a new task, or {@code null} if the command is not a task command
     * @throws SineException if a required task field is missing
     */
    private static Task createTask(String command) throws SineException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new SineException("The description of a todo cannot be empty.");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring(8).trim();
            int byIndex = details.indexOf("/by");
            String description = byIndex < 0 ? details : details.substring(0, byIndex).trim();
            if (description.isEmpty()) {
                throw new SineException("The description of a deadline cannot be empty.");
            }
            if (byIndex < 0 || details.substring(byIndex + 3).trim().isEmpty()) {
                throw new SineException("The deadline of a deadline cannot be empty.");
            }
            return new Deadline(description, details.substring(byIndex + 3).trim());
        }

        if (command.equals("event") || command.startsWith("event ")) {
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
        return null;
    }
}
