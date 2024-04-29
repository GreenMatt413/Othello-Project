import java.util.*;
import java.io.*;
import java.net.*;
public class Player implements Runnable {
    // Create board, input of type Scanner, a socket and output stream, turn boolean
    private final Board board;
    private Scanner input;
    private final Socket soc;
    private final OutputStream os;
    public boolean turn;

    // Create a new socket every time the player constructor is used.
    public Player(String host, int port) throws Exception {
        this(new Socket(host, port));
    }
    // Separate player constructor for each socket that uses each instance field and starts the client
    public Player(Socket soc) throws Exception {
        this.soc = soc;
        this.os = soc.getOutputStream();
        this.board = new Board();
        turn = true;
        startHumanClient();
    }

    // This is for each move
    public void startHumanClient() {
        try {
            input = new Scanner(this.soc.getInputStream()).useDelimiter("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Run method that checks if there is a next token in the input,
    then processes message.
    */
    public void run() {
        try {
                if (input.hasNext()) {
                    String sMove = input.next();
                    processMessage(sMove);
                }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Process message method
    private void processMessage(String m) {
        /* Checks first character, if it's not equal to X
        then it moves to the next character.
        */
        char init = m.charAt(0);
        if (init != 'X') {
            char c1 = m.charAt(1);
            OthelloServer.Move move = new OthelloServer.Move((init - 'A') + 1, (c1 - '1') + 1);
            updateFromServer(move);
        }
        // Set turn
        turn = true;
    }

    /* The strings are converted to an
    array of bytes in memory which are written to an output
    stream for server to interpret.
     */
     public void sendToServer(Board board, int color) throws IOException {
        String n = "MOVE ";
        byte[] b1 = n.getBytes();
        os.write(b1);
        board.writeTo(this.os);

        n = " ";
        b1 = n.getBytes();
        os.write(b1);

        byte two = Board.colorToByte(color);

        os.write(two);

        n = "\r\n";
        b1 = n.getBytes();
        os.write(b1);
        os.flush();
     }
     // This is meant to change the color of the board.
    private void updateBoard(final OthelloServer.Move mv) {
     new Runnable() {
        public void run() {
          try {
              board.move(mv, Board.BLACK);
              sendToServer(board, Board.WHITE);
        } catch (Board.IllegalMoveException | IOException e0) {
              e0.printStackTrace();
        }
        }
        };
    }
    // This is supposed to change the move from server.
     private void updateFromServer(final OthelloServer.Move mv) {
         new Runnable() {
             public void run() {
                 try {
                     // Changes from black to white
                     board.move(mv, Board.WHITE);
                     if (board.endOfGame()) {
                         try {
                             // Closes socket
                             soc.close();
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     }
                     // Prints if wrong move
                 } catch (Board.IllegalMoveException e0) {
                     e0.printStackTrace();
                 }
             }

     };
     }
}
