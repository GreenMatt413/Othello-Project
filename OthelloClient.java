import java.io.*;
import java.net.*;
import java.util.*;

public class OthelloClient {

    class myThread extends Thread {
        public void run() {
            System.out.println("thread is running...");
        }
    }
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 9999);
        BufferedWriter write = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        BufferedReader read = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
}
