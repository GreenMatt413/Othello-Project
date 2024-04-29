import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

    public class Player2 implements HP {
        // Create socket, input and output streams, etc.
        private final Socket soc;

        private final InputStream is;
        private final OutputStream os;

        private final Random random;
        private final boolean isQuiet;

        // Create a new socket for each player 2 move
        public Player2(String host, int port) throws Exception {
            this(new Socket(host, port));
        }

        // Socket instances
        public Player2(Socket soc) throws Exception {
            this.soc = soc;
            this.is = soc.getInputStream();
            this.os = soc.getOutputStream();
            this.random = new Random();
            this.isQuiet = false;
        }

        // Like in player 1 but in player 2.
        public void sendToServer(Board board, int color) throws IOException {
            String n = "MOVE ";
            byte[] b1 = n.getBytes();
            this.os.write(b1);
            board.writeTo(this.os);

            // Empty string check
            n = " ";
            b1 = n.getBytes();
            this.os.write(b1);

            byte two = Board.colorToByte(color);

            this.os.write(two);
            // Carriage return and newline
            n = "\r\n";
            b1 = n.getBytes();
            this.os.write(b1);
            this.os.flush();
        }

        public OthelloServer.Move receiveFromServer() {
            // Detect character used in move from server.
            Scanner input = new Scanner(this.is).useDelimiter("\r\n");
            String serverMove = input.next();
            char c0 = serverMove.charAt(0);
            // When the server has an X move, a new move is triggered.
            if (c0 == 'X')
                return new OthelloServer.Move();
            else {
                char c1 = serverMove.charAt(1);
                OthelloServer.Move move = new OthelloServer.Move((c0 - 'A') + 1, (c1 - '1') + 1);
                return move;
            }
        }

        // Check connection status with chars
        public String checkChars() throws IOException {
            byte[] a = new byte[8];
            this.is.read(a);
            char first = (char) a[0];
            this.is.read(a);
            char second = (char) a[0];
            return first + String.valueOf(second);
        }

        // Play a move. (synchronized is due to asynchronous server and clients.
        public synchronized OthelloServer.Move play(Board board, int color) {
            // Print colors on board
            board.print(color);
            // If move is not playable, skip
            if(!board.isPlayable(color)) {
                System.out.println("You've been skipped!");
                return new OthelloServer.Move();
            }
            List<OthelloServer.Move> moves = board.legalMoves(color);
            // If no moves have been made, make a new move
            if (moves.isEmpty()) {
                return new OthelloServer.Move();
            }
            /* Create a random integer based on how many moves to be used
            in creating new move.
            */
            final int i = random.nextInt(moves.size());
            final OthelloServer.Move mv = moves.get(i);
            // Print move to screen.
            if (!isQuiet) {
                System.out.println("Player 1 played: " + mv);
            }
            return mv;
        }
        // Close socket
        public void close() throws IOException {
            soc.close();
        }
    }
