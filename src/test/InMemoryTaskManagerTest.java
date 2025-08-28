/*import manager.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import util.enumConstant.Status;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends InMemoryTaskManager {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testManagerCreation() {
        Task task = new Task("task","decr",Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        taskManager.createTask(task);
        assertNotNull(task);
        assertEquals(1,task.getId());

        Epic epic = new Epic("epic","decr");
        taskManager.createEpic(epic);
        assertNotNull(epic);
        assertEquals(2,epic.getId());

        SubTask subTask = new SubTask("subtask","decr",Status.NEW,Duration.ofMinutes(10),LocalDateTime.now().plus(Duration.ofMinutes(1000)), epic.getId());
        taskManager.createSubTask(subTask);
        assertNotNull(subTask);
        assertEquals(3,subTask.getId());

    }
}*/

