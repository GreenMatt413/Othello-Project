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
            while(true){
                try{

            bw.write("Starting Othello Game....... ");
            bw.newLine();
            bw.flush();

            int[][] othelloBoard = new int[8][8];
            othelloBoard[3][3]= 1; //Player 1
            othelloBoard[4][4]= 1;
            othelloBoard[3][4]= 2; //Player 2
            othelloBoard[4][3]= 2;

            bw.write("Test");
            bw.newLine();
            bw.flush();
            // for (int i=0;i<othelloBoard.length;i++){
            //     for (int j=0;j<othelloBoard.length;j++){
            //         bw.write(othelloBoard[i][j]);
            //     }
            //     bw.newLine();
            // }

            System.out.println("Server Closed!");
            this.br.close();
            this.bw.close();
                }
            catch(IOException e){}
        }
    }
    }
