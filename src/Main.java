import model.*;
import manager.*;
import util.enumConstant.Status;
import java.io.FileReader;
import java.io.*;


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

        TaskManager manager = FileBackedTaskManager.loadFromFile(file);

        System.out.println(manager.getTask(1));
        System.out.println(manager.getSubTasksByEpicId(2));




    }
}
