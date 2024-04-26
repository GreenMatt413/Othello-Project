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
        Socket soc = new Socket("localhost", 2048);
        BufferedWriter write = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
        BufferedReader read = new BufferedReader(new InputStreamReader(soc.getInputStream()));

        Scanner input = new Scanner(System.in);

        System.out.println(read.readLine());

        write.close();
        read.close();
        soc.close();


    }
}
