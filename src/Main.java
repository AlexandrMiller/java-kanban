import http.HttpTaskServer;
import manager.*;

import java.io.*;


public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = Managers.getDefault();
        try {

        HttpTaskServer ss = new HttpTaskServer(manager);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
