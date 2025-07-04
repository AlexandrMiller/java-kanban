package manager;
import model.Task;


import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory(){

        return history;
    }

        @Override
    public void addToHistoryTask(Task task){
        if(history.size() == 10) {
            history.remove(0);
        }
        history.add(task);

    }



}
