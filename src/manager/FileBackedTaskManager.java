package manager;
import model.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import exceptions.ManagerSaveException;
import util.enumConstant.*;


public class FileBackedTaskManager extends InMemoryTaskManager {
  private final String header = "id,type,name,status,duration,startTime,description,epic\n";
  private File saveFile;


  public FileBackedTaskManager(File saveFile) {
      super();
      this.saveFile = saveFile;
  }


  @Override
  public void createTask(Task task) {
      super.createTask(task);
      save();
  }

  @Override
  public Task getTask(int id) {
      return super.getTask(id);
  }

  @Override
  public LocalDateTime getEndTimeOfEpic(Epic epic) {
      return super.getEndTimeOfEpic(epic);
  }

  @Override
  public SubTask getSubTask(int id) {
      return super.getSubTask(id);
  }


  @Override
  public void createSubTask(SubTask subTask) {
      super.createSubTask(subTask);
      save();
  }


  @Override
  public void createEpic(Epic epic) {
      super.createEpic(epic);
      save();
  }

  @Override
  public void deleteTask(int id) {
      super.deleteTask(id);
      save();
  }

  @Override
  public void deleteEpic(int id) {
      super.deleteEpic(id);
      save();
  }

  @Override
  public void deleteSubTask(int id) {
      super.deleteSubTask(id);
      save();
  }


  public void save() {
      try (FileWriter fileWriter = new FileWriter(saveFile)) {
          fileWriter.write(header);

          for (Map.Entry<Integer,Task> entry : getTasks().entrySet()) {
              fileWriter.write(toString(entry.getValue()) + "\n");
          }

          for (Map.Entry<Integer,Epic> entry : getEpics().entrySet()) {
              fileWriter.write(toString(entry.getValue()) + "\n");
          }

          for (Map.Entry<Integer,SubTask> entry : getSubTasks().entrySet()) {
              fileWriter.write(toString(entry.getValue()) + "\n");
          }
      } catch (IOException e) {
          throw new ManagerSaveException("Ошибка сохранения файла");
       }
  }

  public String toString(Task task) {
      String result = "";

      switch (task.getType()) {

          case TASK:
              result += task.getId() + "," + task.getType() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDuration() + "," + task.getStartTime() + "," +
                        task.getDescription();
              break;

          case EPIC:
              result += task.getId() + "," + task.getType() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDuration() + "," + task.getStartTime() + "," +
                        task.getDescription();
              break;

          case SUBTASK:
              result += task.getId() + "," + task.getType() + "," +
                        task.getName() + "," + task.getStatus() + "," +
                        task.getDuration() + "," + task.getStartTime() + "," +
                        task.getDescription() + "," + getSubTask(task.getId()).getEpicId();
              break;

          default:
              return null;
      }
      return result;
  }

  public static Task fromString(String value) {
      String[] taskData = value.split(",");
      Integer id = Integer.valueOf(taskData[0]);
      Types type = Types.valueOf(taskData[1]);
      String name = taskData[2];
      Status status = Status.valueOf(taskData[3]);
      Duration duration = Duration.parse(taskData[4]) ;
      LocalDateTime startTime = LocalDateTime.parse(taskData[5]);
      String description = taskData[6];
      Integer epicId = 0;
      if (taskData.length > 7) {
      String epicIdString = taskData[7];
        if (!epicIdString.isEmpty()) {
            epicId = Integer.valueOf(epicIdString);
        }
      }


      switch (type) {
          case TASK:
              Task task = new Task(name,description,status,duration,startTime);
              task.setId(id);
              return task;

          case SUBTASK:
              SubTask subTask = new SubTask(name,description,status,duration,startTime,epicId);
              subTask.setId(id);
              return subTask;

          case EPIC:
              Epic epic = new Epic(name,description);
              epic.setId(id);
              epic.setStatus(status);
              return epic;

          default:
              return  null;
      }
  }

    public static FileBackedTaskManager loadFromFile(File file) {
        TaskManager taskManager = Managers.getDefault();


        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            List<String> strings = new ArrayList<>();

            while (bufferedReader.ready()) {
                strings.add(bufferedReader.readLine());
            }

            for (String string : strings) {
                if (!string.isBlank()) {
                    Object task = fromString(string);

                    if (task instanceof SubTask) {
                        taskManager.createSubTask((SubTask) task);
                        fileBackedTaskManager.createSubTask((SubTask) task);
                    } else if (task instanceof Epic) {
                        taskManager.createEpic((Epic) task);
                        fileBackedTaskManager.createEpic((Epic) task);
                    } else if (task instanceof Task) {
                        taskManager.createTask((Task) task);
                        fileBackedTaskManager.createTask((Task) task);
                    }



                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

        return fileBackedTaskManager;
    }
}

