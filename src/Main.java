import model.*;
import manager.*;
import util.enumConstant.Status;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Epic epic;
        SubTask subTask;
        Task task;
        TaskManager taskManager = Managers.getDefault();


        task = new Task("task1", "decr1", Status.NEW);
        taskManager.createTask(task);
        task = new Task("task2", "decr2",Status.NEW);
        taskManager.createTask(task);

        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getTask(1));

        System.out.println("history:");
        System.out.println(taskManager.getHistory());
        System.out.println("получаю повторный таск");

        System.out.println(taskManager.getTask(2));

        System.out.println("historyV2:");
        System.out.println(taskManager.getHistory());
    }
}
