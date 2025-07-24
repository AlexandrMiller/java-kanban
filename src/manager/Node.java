package manager;

import model.Task;

class Node {
    private Task task;
    private Node next;
    private Node prev;

    Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}