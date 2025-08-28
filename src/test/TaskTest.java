import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.Task;
import util.enumConstant.Status;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testTasksAreEqualById() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        task1.setId(1);

        Task task2 = new Task("Task 1", "Description 1", Status.NEW,Duration.ofMinutes(10),LocalDateTime.now());
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    void testEpicAreEqualById() {
        Epic epic = new Epic("epic1","decr2");
        epic.setId(1);

        Epic epic2 = new Epic("epic1","decr2");
        epic2.setId(1);

        assertEquals(epic,epic2);
    }

    @Test
    void testSubtaskAreEqualsByUId() {
        SubTask subTask = new SubTask("sub1","decr1",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now(),1);

        SubTask subTask1 = new SubTask("sub1","decr1",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now(),1);

        assertEquals(subTask1,subTask);

    }

    @Test
    void epicCannotContainItselfAsSubtask() {
        Epic epic = new Epic("Epic", "Desc");
        epic.setId(10);

    }

    @Test
    void subtaskCreationAndGettersShouldWork() {
        int epicId = 10;
        SubTask subtask = new SubTask("Test Subtask", "Description", Status.NEW,Duration.ofMinutes(10),LocalDateTime.now(),10);

        assertEquals("Test Subtask", subtask.getName());
        assertEquals("Description", subtask.getDescription());
        assertEquals(epicId, subtask.getEpicId());
        assertEquals(Status.NEW, subtask.getStatus());
    }

    @Test
    void epicCreationAndGettersShouldWork() {
       Epic epic = new Epic("epic","decr");
       epic.setId(1);
       assertEquals("epic", epic.getName());
       assertEquals("decr",epic.getDescription());
       assertEquals(1,epic.getId());
       assertEquals(Status.NEW,epic.getStatus());
    }

    @Test
    void taskCreationAndGettersShouldWork() {
        Task task = new Task("task","decr",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now());
        task.setId(1);

        assertEquals("task",task.getName());
        assertEquals("decr",task.getDescription());
        assertEquals(Status.NEW,task.getStatus());
        assertEquals(1,task.getId());
    }
}
