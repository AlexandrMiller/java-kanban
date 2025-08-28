import manager.*;
import model.Epic;
import model.SubTask;
import model.Task;
import util.enumConstant.Status;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("C:/Users/mille/sprint5/java-kanban/text.csv");
        try {
            if (file.createNewFile()) {
                System.out.println("file is created");
            } else {
                System.out.println("file is already created");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FileBackedTaskManager ff = FileBackedTaskManager.loadFromFile(file);

        /* Task task = new Task("task","descr",Status.NEW,Duration.ofMinutes(60),LocalDateTime.of(2000,2,2,10,0));
        ff.createTask(task);
        Epic epic = new Epic("epic","descrofepic");
        ff.createEpic(epic);
        SubTask sub = new SubTask("sub","descrofsub",Status.NEW,Duration.ofMinutes(60),LocalDateTime.of(2025,1,1,10,00),2);
        SubTask sub1 = new SubTask("sub2","descrofsub2",Status.IN_PROGRESS,Duration.ofMinutes(60),LocalDateTime.of(2025,1,1,10,10),2);
        SubTask sub2 = new SubTask("sub3","descrofsub3",Status.DONE,Duration.ofMinutes(60),LocalDateTime.of(2025,2,1,10,00),2);
        ff.createSubTask(sub);
        ff.createSubTask(sub2); */
        System.out.println(ff.getPrioritizedTasks());
        System.out.println(ff.getEpic(2).getStartTime());
        System.out.println(ff.getEndTimeOfEpic(ff.getEpic(2)));
    }
}
