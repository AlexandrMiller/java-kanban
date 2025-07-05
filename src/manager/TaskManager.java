package manager;

import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;

import java.util.HashMap;

public interface TaskManager {


     HashMap<Integer, Task> getTasks();

     HashMap<Integer, SubTask> getSubTasks();

     HashMap<Integer, Epic> getEpics();

     HashMap<Integer, SubTask> getSubTasksByEpicId(int id);

     List<Task> getHistory();

     int getId();

     void deleteAllTasks();

     void deleteAllEpics();

     void deleteAllSubTasks();

     Task getTask(int id);

     SubTask getSubTask(int id);

     Epic getEpic(int id);

     void createTask(Task task);

     void createSubTask(SubTask subTask);

     void createEpic(Epic epic);

     void deleteTask(int id);

     void deleteEpic(int id);

     void deleteSubTask(int id);

     void updateTask(Task task);

     void updateSubTask(SubTask subTask);

     void updateEpic(Epic epic);

     void updateStatusEpic(Epic epic);
}
