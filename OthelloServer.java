import java.io.*;
import java.net.*;

public class OthelloServer {
    public static void main(String[] args) throws IOException {
        ServerSocket se = new ServerSocket(2048);
    while(true){
        try{
            Socket so = se.accept();

            System.out.println("Client Connected!"+so);

            BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));

            Thread t = new ControlClient(so, br, bw);

            t.start();
        }
        catch(Exception e){}

    }
    }
}
    class ControlClient extends Thread{
    final BufferedReader br;
        final BufferedWriter bw;
        final Socket so;

        public ControlClient(Socket s, BufferedReader br, BufferedWriter bw){
            this.so = s;
            this.br = br;
            this.bw = bw;
        }

        public void run(){

        }

    }
