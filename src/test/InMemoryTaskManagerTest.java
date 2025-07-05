package test;

import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.enumConstant.*;






class InMemoryTaskManagerTest extends InMemoryTaskManager {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
    taskManager = Managers.getDefault();
}

    @Test
    void testManagerCreation(){
        Task task = new Task("task","decr",Status.NEW);
        taskManager.createTask(task);
        assertNotNull(task);
        assertEquals(1,task.getId());

        Epic epic = new Epic("epic","decr");
        taskManager.createEpic(epic);
        assertNotNull(epic);
        assertEquals(2,epic.getId());

        SubTask subTask = new SubTask("subtask","decr",Status.NEW,epic.getId());
        taskManager.createSubTask(subTask);
        assertNotNull(subTask);
        assertEquals(3,subTask.getId());

    }



    }

