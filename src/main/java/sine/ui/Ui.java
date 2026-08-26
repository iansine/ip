package sine.ui;

import java.util.Scanner;

import sine.task.Task;
import sine.task.TaskList;

/**
 * Handles all console input and output for the Sine chatbot.
 */
public class Ui {
    private static final String SEPARATOR =
            "____________________________________________________________";
    private static final String BANNER = " ____  _            \n"
            + "/ ___|(_)_ __   ___ \n"
            + "\\___ \\| | '_ \\ / _ \\\n"
            + " ___) | | | | |  __/\n"
            + "|____/|_|_| |_|\\___|";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the chatbot banner and greeting.
     */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Sine.");
        System.out.println("What's up?");
        System.out.println(SEPARATOR);
    }

    /**
     * Checks whether another command is available from the input stream.
     *
     * @return true if another command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command and starts its response section.
     *
     * @return command entered by the user
     */
    public String readCommand() {
        String command = scanner.nextLine();
        System.out.println(SEPARATOR);
        return command;
    }

    /**
     * Shows the normal farewell.
     */
    public void showGoodbye() {
        System.out.println(" Bye. I'll be here if you need me :)");
        System.out.println(SEPARATOR);
    }

    /**
     * Shows every task with its one-based task number.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(" TODO list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Shows confirmation that a task was removed.
     *
     * @param task removed task
     * @param taskCount number of remaining tasks
     */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println(" Roger that. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task updated task
     */
    public void showUnmarkedTask(Task task) {
        System.out.println(" Roger that. I've marked this task as not done yet:");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task updated task
     */
    public void showMarkedTask(Task task) {
        System.out.println(" Great work! I've marked this task as done:");
        System.out.println("   " + task);
        System.out.println(SEPARATOR);
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param task added task
     * @param taskCount number of current tasks
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Shows the response for an unknown command.
     */
    public void showUnknownCommand() {
        System.out.println(" Must have been the wind");
        System.out.println(SEPARATOR);
    }

    /**
     * Shows a user-command validation error.
     *
     * @param message explanation of the invalid command
     */
    public void showCommandError(String message) {
        System.out.println(" Error :( " + message);
        System.out.println(SEPARATOR);
    }

    /**
     * Warns that saved tasks could not be loaded.
     */
    public void showLoadingError() {
        System.out.println(" Warning: I couldn't load your saved tasks."
                + " Starting with an empty list.");
        System.out.println(SEPARATOR);
    }

    /**
     * Warns that a task-list change could not be saved.
     */
    public void showSavingError() {
        System.out.println(" Error :( I couldn't save that change."
                + " It is available only for this session.");
        System.out.println(SEPARATOR);
    }
}
