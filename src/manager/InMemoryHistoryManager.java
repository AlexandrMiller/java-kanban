package manager;
import model.Task;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

   private HashMap<Integer,Node> historyMap = new HashMap<>();

    public static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Task task) {
            this.task = task;
        }
    }


    private Node head;
    private Node tail;

    public void linkLast(Node node) {
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
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

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {

            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {

            tail = node.prev;
        }


        historyMap.remove(node.task.getId());
    }


    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
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
