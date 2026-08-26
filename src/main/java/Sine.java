import java.io.IOException;
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

        Storage storage = new Storage("data/sine.txt");
        Parser parser = new Parser();
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            tasks = new TaskList();
            System.out.println(" Warning: I couldn't load your saved tasks."
                    + " Starting with an empty list.");
            System.out.println(SEPARATOR);
        }
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            try {
                Parser.CommandType commandType = parser.getCommandType(command);
                if (commandType == Parser.CommandType.BYE) {
                    System.out.println(" Bye. I'll be here if you need me :)");
                    System.out.println(SEPARATOR);
                    break;
                }

                if (commandType == Parser.CommandType.LIST) {
                    System.out.println(" TODO list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println(SEPARATOR);
                    continue;
                }

                if (commandType == Parser.CommandType.DELETE) {
                    int taskIndex = parser.parseTaskIndex(command, tasks.size());
                    Task removedTask = tasks.delete(taskIndex);
                    storage.save(tasks.getTasks());
                    System.out.println(" Roger that. I've removed this task:");
                    System.out.println("   " + removedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(SEPARATOR);
                    continue;
                }

                if (commandType == Parser.CommandType.UNMARK) {
                    int taskIndex = parser.parseTaskIndex(command, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks.getTasks());
                    System.out.println(" Roger that. I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskIndex));
                    System.out.println(SEPARATOR);
                    continue;
                }

                if (commandType == Parser.CommandType.MARK) {
                    int taskIndex = parser.parseTaskIndex(command, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks.getTasks());
                    System.out.println(" Great work! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskIndex));
                    System.out.println(SEPARATOR);
                    continue;
                }

                if (commandType == Parser.CommandType.ADD_TASK) {
                    Task newTask = parser.parseTask(command);
                    tasks.add(newTask);
                    storage.save(tasks.getTasks());
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + newTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(SEPARATOR);
                    continue;
                }

                System.out.println(" Must have been the wind");
                System.out.println(SEPARATOR);
            } catch (SineException exception) {
                System.out.println(" Error :( " + exception.getMessage());
                System.out.println(SEPARATOR);
            } catch (IOException exception) {
                System.out.println(" Error :( I couldn't save that change."
                        + " It is available only for this session.");
                System.out.println(SEPARATOR);
            }
        }
    }

}
