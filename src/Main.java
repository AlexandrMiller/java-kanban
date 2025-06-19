import model.*;
import Manager.*;
import util.enumConstant.Status;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();
        Epic epic;
        SubTask subTask;
        Task task;

        System.out.println("EPICS AND SUBTASKS CHECKING:");
        epic = new Epic("Работа в такси", "Устроиться работать таксистом");
        manager.createEpic(epic);
        subTask = new SubTask("Получить права", "Пойти отучиться на права в автошколу",
                Status.NEW, epic.getId());
        manager.createSubTask(subTask);
        subTask = new SubTask("Купить авто","Посетить автосалон для покупки авто",
                Status.NEW, epic.getId());
        manager.createSubTask(subTask);
        subTask = new SubTask("Устроиться таксистом","СХодить в офис таксопарка для трудоустройства",
                Status.NEW, epic.getId());
        manager.createSubTask(subTask);

        epic = new Epic("Спринт 4","Сделать задание 4 спринта");
        manager.createEpic(epic);
        subTask = new SubTask("Проект 4 спринта","Сделать проект 4 спринта до 16.06.",
                Status.NEW, epic.getId());
        manager.createSubTask(subTask);

      /*Проверка работы методов для епиков и сабЗадач*/

        System.out.println(manager.getEpic(1));
        System.out.println(manager.getSubTask(2));
        manager.printSubTasksByEpicId(1);
        manager.deleteSubTask(4);
        System.out.println(manager.getEpic(1));
        manager.deleteEpic(5);


        subTask = manager.getSubTask(2);
        subTask.setStatus(Status.IN_PROGRESS);
        manager.updateSubTask(subTask);
        System.out.println(manager.getSubTask(2));
        manager.printEpics();
        manager.printSubTasks();


        System.out.println("TASKS CHECKING:");
        task = new Task("Покушать","Сходить в кафе",Status.NEW);
        manager.createTask(task);
        task = new Task("Помыться","Сходить в душ",Status.NEW);
        manager.createTask(task);

        /*Проверка работы методов для Задач*/
        System.out.println(manager.getTask(7));
        task = manager.getTask(8);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        System.out.println(manager.getTask(8));
        manager.printTasks();
        System.out.println("Удаляем таск");
        manager.deleteTask(7);
        manager.printTasks();

        /*Удаляем все епики, сабЗадачи, задачи и проверяем*/
        System.out.println("Форматируем все данные");
        manager.deleteAllEpics();
        manager.deleteAllSubTasks();
        manager.deleteAllTasks();
        manager.printTasks();
        manager.printEpics();
        manager.printSubTasks();


    }
}
