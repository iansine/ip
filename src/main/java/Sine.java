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

        String[] tasks = new String[100];
        boolean[] isDone = new boolean[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(SEPARATOR);

            if (command.equals("bye")) {
                System.out.println(" Bye. I'll be here if you need me :)");
                System.out.println(SEPARATOR);
                break;
            }

            if (command.equals("list")) {
                System.out.println(" TODO list:");
                for (int i = 0; i < taskCount; i++) {
                    String status = isDone[i] ? "X" : " ";
                    System.out.println(" " + (i + 1) + ".[" + status + "] " + tasks[i]);
                }
                System.out.println(SEPARATOR);
                continue;
            }

            if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                isDone[taskIndex] = false;
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   [ ] " + tasks[taskIndex]);
                System.out.println(SEPARATOR);
                continue;
            }

            if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                isDone[taskIndex] = true;
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   [X] " + tasks[taskIndex]);
                System.out.println(SEPARATOR);
                continue;
            }

            tasks[taskCount] = command;
            taskCount++;
            System.out.println(" added: " + command);
            System.out.println(SEPARATOR);
        }
    }
}
