package http;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import model.Epic;
import model.SubTask;
import model.Task;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
   private static final int port = 8080;
   InMemoryTaskManager manager;
   HttpServer httpServer;
   BaseHttpHandler bsh = new BaseHttpHandler();
   Gson gson = new GsonBuilder()
           .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
           .registerTypeAdapter(Duration.class, new DurationAdapter())
           .registerTypeHierarchyAdapter(Enum.class, new EnumAdapter<>())
           .create();

   public HttpTaskServer(InMemoryTaskManager manager) throws IOException {
      this.manager = manager;
      this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
      httpServer.createContext("/tasks", new TaskHandler());
      httpServer.createContext("/subtasks", new SubtaskHandler());
      httpServer.createContext("/epic", new EpicHandler());
      httpServer.createContext("/history", new HistoryHandler());
      httpServer.createContext("/priory",new PrioritizedTasksHandler());
      httpServer.start();
   }

   class TaskHandler implements HttpHandler {

      @Override
      public void handle(HttpExchange exchange) throws IOException {

         String method = exchange.getRequestMethod();

         switch (method) {
            case "GET":
               getTasks(exchange);
               break;
            case "POST":
               createTaskOrUpdate(exchange);
               break;
            case "DELETE":
               deleteTasks(exchange);
               break;
            default:
               exchange.sendResponseHeaders(405, -1);
               break;
         }
      }


   }

   class SubtaskHandler implements HttpHandler {

      @Override
      public void handle(HttpExchange exchange) throws IOException {
         String method = exchange.getRequestMethod();

         switch (method) {
            case "GET":
               getSubtasks(exchange);
               break;
            case "POST":
               createOrUpdateSubtask(exchange);
               break;
            case "DELETE":
               deleteSubtasks(exchange);
               break;
            default:
               exchange.sendResponseHeaders(405, -1);
         }
      }
   }

   class EpicHandler implements HttpHandler {

      @Override
      public void handle(HttpExchange exchange) throws IOException {
         String method = exchange.getRequestMethod();

         switch (method) {
            case "GET":
               getEpics(exchange);
               break;
            case "POST":
               createEpic(exchange);
               break;
            case "DELETE":
               deleteEpicById(exchange);
               break;
            default:
               exchange.sendResponseHeaders(405, -1);

         }
      }
   }

   class HistoryHandler implements HttpHandler {

      @Override
      public void handle(HttpExchange exchange) throws IOException {
         String method = exchange.getRequestMethod();

         switch (method) {
            case "GET":
               getHistory(exchange);
               break;
            default:
               bsh.sendText(exchange,"Вы можете только просмотреть историю задач",406);
         }
      }
   }

   class PrioritizedTasksHandler implements HttpHandler {

      @Override
      public void handle(HttpExchange exchange) throws IOException {
         String method = exchange.getRequestMethod();

         switch (method) {
            case "GET":
               getPrioritizedTasks(exchange);
               break;
            default:
               bsh.sendText(exchange,"Вы можете только просмотреть список приоритетных задач",406);
               break;
         }
      }
   }

   public void getPrioritizedTasks(HttpExchange exchange) throws IOException {
      String priory = gson.toJson(manager.getPrioritizedTasksWithoutEpics());
      bsh.sendText(exchange,priory,200);
   }

   public void getHistory(HttpExchange exchange) throws IOException {
      String historyJson = gson.toJson(manager.getHistory());
      bsh.sendText(exchange,historyJson,200);
   }

   public void getEpics(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");

      try {
         if (splitURI.length == 2) {
            String epicJson = gson.toJson(manager.getEpics());
            bsh.sendText(exchange, epicJson, 200);
         } else if (splitURI.length == 3) {
            int id = Integer.parseInt(splitURI[2]);
            if (!(manager.getEpic(id) == null)) {
               String epicJson = gson.toJson(manager.getEpic(id));
               bsh.sendText(exchange, epicJson, 200);
            } else {
               bsh.sendText(exchange,"Такого Эпика нет",404);
            }
         } else if (splitURI.length == 4) {
            int id = Integer.parseInt(splitURI[2]);
            if (!(manager.getSubTasksByEpicId(id) == null)) {
            String epicJson = gson.toJson(manager.getSubTasksByEpicId(id));
            bsh.sendText(exchange, epicJson, 200);
            } else {
               bsh.sendText(exchange,"Такой подзадачи у этого эпика нет",404);
            }
         }
      } catch (Exception e) {
         bsh.sendText(exchange, "Нет такого", 404);
      }
   }

   public void createEpic(HttpExchange exchange) throws IOException {
      InputStream inputStream = exchange.getRequestBody();
      String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
     // Task task = FileBackedTaskManager.fromString(body);
      String[] bodySplit = body.split(",");
      Epic epic = new Epic(bodySplit[0],bodySplit[1]);
      manager.createEpic(epic);
      bsh.sendText(exchange,"Эпик успешно создан",201);
   }

   public void deleteEpicById(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");
      if (splitURI.length == 3) {
         InputStream inputStream = exchange.getRequestBody();
         String body = new String(inputStream.readAllBytes(),StandardCharsets.UTF_8);
         String[] bodySplit = body.split("/");
         int id = Integer.parseInt(bodySplit[2]);
         manager.deleteEpic(id);
         bsh.sendText(exchange,"Эпик удален",200);
      }
   }


   public void getSubtasks(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");

      try {
         if (splitURI.length == 2) {
            String subTaskJson = gson.toJson(manager.getSubTasks());
            bsh.sendText(exchange, subTaskJson, 200);
         } else if (splitURI.length == 3) {
            int id = Integer.parseInt(splitURI[2]);
            String subTaskJson = gson.toJson(manager.getSubTask(id));
            bsh.sendText(exchange, subTaskJson, 200);
         } else {
            bsh.sendText(exchange, "Not Found", 404);
         }
      } catch (Exception e) {
         bsh.sendText(exchange, "Такой подзадачи нет", 404);
      }
   }

   public void createOrUpdateSubtask(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");
      InputStream inputStream = exchange.getRequestBody();
      String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      String[] bodySplit = body.split(",");
      if (!bodySplit[1].equals("SUBTASK")) {
         bsh.sendText(exchange, "По этому запросу можно создать только SUBTASK", 406);
      } else {
         if (splitURI.length == 2) {
            Task task = FileBackedTaskManager.fromString(body);
            if (manager.isOverlap(task)) {
               bsh.sendText(exchange, "В это время у вас уже есть задача", 406);
            } else {
               manager.createSubTask((SubTask) task);
               String success = "Подзадача успешно добавлена";
               bsh.sendText(exchange, success, 201);
            }
         } else if (splitURI.length == 3) {
            int id = Integer.parseInt(splitURI[2]);
            manager.updateSubTask(manager.getSubTask(id));
            bsh.sendText(exchange, "Подзадача успешно обновлена", 201);
         }
      }
   }

   public void deleteSubtasks(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");
      int id = Integer.parseInt(splitURI[2]);
      manager.deleteSubTask(id);
      try {
         bsh.sendText(exchange, "Задача удалена", 200);
      } catch (Exception e) {
         bsh.sendText(exchange, "Введите ID задачи", 406);
      }
   }


   public void getTasks(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");

      try {
         if (splitURI.length == 2) {

            String tasksJson = gson.toJson(manager.getTasks());
            bsh.sendText(exchange, tasksJson, 200);
         } else if (splitURI.length == 3) {
            int id = Integer.parseInt(splitURI[2]);
            String taskJson = gson.toJson(manager.getTask(id));
            bsh.sendText(exchange, taskJson, 200);
         } else {
            String response = "Not found";
            bsh.sendText(exchange, response, 404);
         }
      } catch (Exception e) {
         String response = "Такой задачи нет";
         bsh.sendText(exchange, response, 404);
      }
   }

   public void createTaskOrUpdate(HttpExchange exchange) throws IOException {
      String[] splitURI = exchange.getRequestURI().getPath().split("/");
      InputStream inputStream = exchange.getRequestBody();
      String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      String[] bodySplit = body.split(",");
       if (!bodySplit[1].equals("TASK")) {
       bsh.sendText(exchange,"По этому запросу можно создать только TASK",406);
       } else {
      if (splitURI.length == 2) {
         Task task = FileBackedTaskManager.fromString(body);
         if (manager.isOverlap(task)) {
            bsh.sendText(exchange, "В это время у вас уже есть задача", 406);
         } else {
            manager.createTask(task);
            String success = "Задача успешно добавлена";
            bsh.sendText(exchange, success, 201);
         }
      } else if (splitURI.length == 3) {
         int id = Integer.parseInt(splitURI[2]);
         manager.updateTask(manager.getTask(id));
         bsh.sendText(exchange, "Задача успешно обновлена", 201);
         }
      }
   }

   public void deleteTasks(HttpExchange exchange) throws IOException {
      try {
         String[] splitURI = exchange.getRequestURI().getPath().split("/");
         int id = Integer.parseInt(splitURI[2]);
         manager.deleteTask(id);

         bsh.sendText(exchange, "Задача удалена", 200);
      } catch (Exception e) {
         bsh.sendText(exchange,"Введите ID задачи",406);
      }
   }
}

