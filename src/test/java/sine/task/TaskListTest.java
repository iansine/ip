package sine.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests task-list mutation, indexing, and collection encapsulation.
 */
public class TaskListTest {
    @Test
    public void constructor_noInitialTasks_createsEmptyList() {
        TaskList tasks = new TaskList();

        assertEquals(0, tasks.size());
        assertTrue(tasks.getTasks().isEmpty());
    }

    @Test
    public void addAndGet_multipleTasks_preservesOrderAndIdentity() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("first");
        Todo second = new Todo("second");

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertSame(first, tasks.get(0));
        assertSame(second, tasks.get(1));
    }

    @Test
    public void delete_existingTask_returnsTaskAndClosesIndexGap() {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));

        Task removedTask = tasks.delete(0);

        assertSame(first, removedTask);
        assertEquals(1, tasks.size());
        assertSame(second, tasks.get(0));
    }

    @Test
    public void constructor_sourceListChanges_doesNotChangeTaskList() {
        List<Task> source = new ArrayList<>();
        source.add(new Todo("original"));
        TaskList tasks = new TaskList(source);

        source.add(new Todo("later"));

        assertEquals(1, tasks.size());
    }

    @Test
    public void getTasks_taskListChanges_returnsUnmodifiableSnapshot() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("original"));
        List<Task> snapshot = tasks.getTasks();

        tasks.add(new Todo("later"));

        assertEquals(1, snapshot.size());
        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.add(new Todo("not allowed")));
    }
}
