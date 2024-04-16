import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class OthelloServer {
    public static void main(String[] args) throws IOException {
        ServerSocket se = new ServerSocket(9999);
        System.out.println("Waiting for client connection...");
        while(true) {
            Socket s = se.accept();
            try {
                System.out.println("Connected to client." + s);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            } catch (IOException e){
               e.printStackTrace();
            }
        }
    }
}