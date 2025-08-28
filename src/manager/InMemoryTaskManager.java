package manager;
import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;
import model.*;
import util.enumConstant.*;


public class InMemoryTaskManager implements TaskManager {
    private int id;
    private HistoryManager historyManager;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = Comparator.comparing(
                Task::getStartTime,Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getId);

        TreeSet<Task> sortedTasks = new TreeSet<>(comparator);
        sortedTasks.addAll(tasks.values());
        sortedTasks.addAll(subTasks.values());
        sortedTasks.addAll(epics.values());

        return sortedTasks;

    }

    public TreeSet<Task> getPrioritizedTasksWithoutEpics() { //для использования в методе isOverlap
        Comparator<Task> comparator = Comparator.comparing(
                        Task::getStartTime,Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getId);

        TreeSet<Task> sortedTasks = new TreeSet<>(comparator);
        sortedTasks.addAll(tasks.values());
        sortedTasks.addAll(subTasks.values());

        return sortedTasks;
    }

    public boolean isOverlap(Task task) {
        boolean overlap = false;
        for (Task ss : getPrioritizedTasksWithoutEpics()) {
            boolean isOverlap = task.getStartTime().isBefore(ss.getStartTime()) &&
                    task.getEndTime().isBefore(ss.getStartTime()) ||
                    task.getStartTime().isAfter(ss.getStartTime()) && task.getStartTime().isAfter(ss.getEndTime());
            if (isOverlap) {
               overlap = false;
            } else {
                overlap = true;
            }
        } return overlap;
    }

    public LocalDateTime getEndTimeOfEpic(Epic epic) {
        LocalDateTime endTime = null;
        for (SubTask sub : getSubTasksByEpicId(epic.getId()).values()) {
            if (endTime == null || sub.getEndTime().isAfter(endTime)) {
                endTime = sub.getEndTime();
            }
        } return endTime;
    }

    public  InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Map<Integer, Task> getTasks() {

        return tasks;
    }

    public void updateDuration(Epic epic) {
        Duration totalDuration = Duration.ZERO;
        for (SubTask sub : getSubTasksByEpicId(epic.getId()).values()) {
            totalDuration = totalDuration.plus(sub.getDuration());
        }
        epic.setDuration(totalDuration);
        epic.setEndTime(epic.getStartTime().plus(totalDuration));
    }

    public void updateStartTimeEpic(Epic epic) {
        LocalDateTime minTime = null;
        for (SubTask sub : getSubTasksByEpicId(epic.getId()).values()) {
            if (minTime == null || sub.getStartTime().isBefore(minTime)) {
                minTime = sub.getStartTime();
            }
        }
        epic.setStartTime(minTime);
    }

    @Override
    public Map<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        for (Epic epics : epics.values()) {
            historyManager.addToHistoryTask(epics);
        }
        return epics;
    }


    @Override
    public Map<Integer, SubTask> getSubTasksByEpicId(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        Map<Integer, SubTask> result = new HashMap<>();
        for (Integer subTaskId : epic.getSubTasksEpic()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask != null) {
                result.put(subTaskId, subTask);
            }
        }
        return result;
    }



    @Override
    public int getId() {
        return ++id;
    }

    public void printTasks() {

        if (tasks.size() == 0) System.out.println("Список задач пуст.");
        for (Task name : tasks.values()) {

            System.out.println("Task.name: " + name.getName());
        }
    }

    public void printEpics() {

        if (epics.size() == 0) System.out.println("Список эпиков пуст.");
        for (Epic name : epics.values()) {

            System.out.println("Epic.name: " + name.getName());
        }
    }

    public void printSubTasks() {

        if (subTasks.size() == 0) System.out.println("Список подзадач пуст.");
        for (SubTask name : subTasks.values()) {
            System.out.println("SubTask.name: " + name.getName());
        }
    }

    public void printSubTasksByEpicId(int id) {

        if (epics.get(id) == null) {
            System.out.println("Список подзадач пуст.");
        } else {
            System.out.println(getSubTasksByEpicId(id));
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksEpic().clear();
            updateStatusEpic(epic);
            updateStartTimeEpic(epic);
            updateDuration(epic);
        }
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.addToHistoryTask(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.addToHistoryTask(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.addToHistoryTask(epic);
        return epic;
    }

    @Override
    public void createTask(Task task) {
        if (isOverlap(task)) {
            System.out.println("В это время у вас уже есть задача: " +
                    task.getStartTime() + " : " + task.getEndTime());
        } else {
            task.setId(getId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (isOverlap(subTask)) {
            System.out.println("В это время у вас уже есть задача: " +
                    subTask.getStartTime() + " : " + subTask.getEndTime());
        } else {
            subTask.setId(getId());
            Epic epic = epics.get(subTask.getEpicId());
          if (epic != null) {
              subTasks.put(subTask.getId(), subTask);
              epic.getSubTasksEpic().add(subTask.getId());
              updateStatusEpic(epic);
              updateStartTimeEpic(epic);
              updateDuration(epic);
          } else {
            System.out.println("Эпик не найден.");
          }
        }
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(getId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.get(id) != null) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTasksEpic()) {
                subTasks.remove(subTaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Эпик не найден.");
        }
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTasksEpic().remove(Integer.valueOf(id));
                updateStatusEpic(epic);
            }
            subTasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Подзадача не найдена.");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                updateStatusEpic(epic);
            } else {
                System.out.println("Эпик не найден для этой подзадачи.");
            }
        } else {
            System.out.println("Подзадача не найдена.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateStatusEpic(epic);
        } else {
            System.out.println("Эпик не найден.");
        }
    }

    @Override
    public void updateStatusEpic(Epic epic) {
        if (epic != null) {
            List<Integer> subTaskIds = epic.getSubTasksEpic();

            if (subTaskIds.isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }

            int countDone = 0;
            int countNew = 0;

            for (Integer id : subTaskIds) {
                SubTask sub = subTasks.get(id);
                if (sub != null) {
                    if (sub.getStatus() == Status.DONE) {
                        countDone++;
                    } else if (sub.getStatus() == Status.NEW) {
                        countNew++;
                    } else if (sub.getStatus() == Status.IN_PROGRESS) {
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                    }
                }
            }

            if (countDone == subTaskIds.size()) {
                epic.setStatus(Status.DONE);
            } else if (countNew == subTaskIds.size()) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

}