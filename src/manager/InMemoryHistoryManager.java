package manager;
import model.Task;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer,Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    public void linkLast(Node node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
            tail = node;
        }
    }

    @Override
    public void addToHistoryTask(Task task) {
        if (historyMap.containsKey(task.getId())) {
           removeNode(historyMap.get(task.getId()));
        }
        Node newNode = new Node(task);
        linkLast(newNode);
        historyMap.put(task.getId(),newNode);
    }

    private void removeNode(Node node) {
        if (node == null) return;

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }

        historyMap.remove(node.getTask().getId());
    }


    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.getTask());
            current = current.getNext();
        }
        return history;
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = historyMap.remove(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }
}


