package manager;
import java.util.List;
import model.Task;

public interface HistoryManager {
    List<Task> getHistory();

    void addToHistoryTask(Task task);

    void remove(int id);

}
