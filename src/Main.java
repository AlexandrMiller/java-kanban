import model.*;
import manager.*;
import util.enumConstant.Status;
import java.util.List;
import java.util.function.DoubleToIntFunction;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Epic epic;
        SubTask subTask;
        Task task;
        TaskManager taskManager = Managers.getDefault();

        epic = new Epic("epic1","descriptionOfEpic1");
        taskManager.createEpic(epic);
        subTask = new SubTask("Subtask1","descr.Sub1",Status.NEW, epic.getId());
        taskManager.createSubTask(subTask);
        epic = new Epic("epic2","descrOfEpic2");
        taskManager.createEpic(epic);
        System.out.println(taskManager.getEpic(1));
        System.out.println(taskManager.getEpic(1));
        System.out.println(taskManager.getEpic(1));
        System.out.println(taskManager.getEpic(1));
        System.out.println(taskManager.getSubTask(2));
        System.out.println(taskManager.getEpics());
        System.out.println("history:");
        System.out.println(taskManager.getHistory());




        /*Проверка работы методов для Задач*/




    }
}
