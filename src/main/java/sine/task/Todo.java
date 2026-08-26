package sine.task;

/**
 * Represents a task without an attached date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Formats this task with the {@code T} type icon.
     *
     * @return the formatted todo task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
