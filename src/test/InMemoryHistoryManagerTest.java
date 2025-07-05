package test;
import manager.*;

import org.junit.jupiter.api.Test;
import model.*;
import util.enumConstant.Status;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void historyManagerSaveTest(){

    }
    @Test
    void historyManagerShouldRetainOriginalTaskState() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager manager = Managers.getDefault();

        Task task = new Task("task", "decr", Status.NEW);
        manager.createTask(task);
        historyManager.addToHistoryTask(task);

        assertEquals(task,historyManager.getHistory().get(0));
    }
}