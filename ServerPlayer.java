import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
public class ServerPlayer implements HP {
    // Set max number of threads to 20
    private final int MAX_THREADS = 20;
    private final Random generator;

    // Check if move is made
    private final boolean activity;

    // Create new server socket
    private final ServerSocket soc;

    // Manages number of threads in server
    private final ExecutorService threadManager;

    // Defined fields in constructor
    ServerPlayer(int port) throws Exception {
        soc = new ServerSocket(port);
        this.generator = new Random();
        this.activity = false;
        this.threadManager = Executors.newFixedThreadPool(MAX_THREADS);
    }

    @Override
    public OthelloServer.Move play(Board board, int color) {
        List<OthelloServer.Move> moveCollection = board.legalMoves(color);
        // If no moves, make another move.
        if (moveCollection.isEmpty()) {
            return new OthelloServer.Move();
        }
        // Check for random index and retrieve it, with move.
        final int i = generator.nextInt(moveCollection.size());
        final OthelloServer.Move mv = moveCollection.get(i);
        if (!activity) {
            System.out.println("Random player's move is: " + mv);
        }
        return mv;
    }
    // Tests socket programming in server
    static class setSocket implements Runnable {
        private final Socket conn;
        public setSocket(Socket s) {
            conn = s;
        }
        private void IO(InputStream is, OutputStream os) {
            // New scanner to detect input
            Scanner s = new Scanner(is).useDelimiter("\\s+");
            try {
                while (true) {
                    char tmp = s.next().charAt(0);
                    // If Q is typed it will quit
                    if (tmp == 'Q') {
                        System.out.println("Closing connection...");
                        break;
                    } else {
                        // 8 by 8 board, 10 by 10 array, and it matches value to
                        // char
                        String boardString = s.next();
                        int[][] bArray = new int[10][10];
                        for (int i = 1; i <= 8; i++) {
                            for (int j = 1; j <= 8; j++) {
                                int index = (j-1) + (i-1) * 8;
                                // Check char at specific index throughout board
                                if (boardString.charAt(index) == '-') {
                                    bArray[j][i] = 0;
                                } else if (boardString.charAt(index) == 'X') {
                                    bArray[j][i] = 1;
                                } else if (boardString.charAt(index) == 'O') {
                                    bArray[j][i] = 2;
                                }
                            }
                        }
                        // Converts 2D array into String
                        System.out.println(Arrays.deepToString(bArray));
                    }
                }
            } catch (NoSuchElementException ignored) {

            }
        }

        // How server will run
        @Override
        public void run() {
                try {
                    final InputStream input = conn.getInputStream();
                    final OutputStream output = conn.getOutputStream();
                    // test input and output
                    IO(input, output);
                    // close socket
                    conn.close();
                    System.out.println("Game is finished");
                } catch (IOException e) {
                    try {
                        // close socket if not already closed.
                        conn.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
        // Tests server connection.
    public void testConnection() {
        try {
            while (true) {
                final Socket conn = soc.accept();
                System.out.println("Connection: " + soc.getLocalSocketAddress());
                try {
                    // Make sure thread executes with socket
                    threadManager.execute(new setSocket(conn));
                } catch (Exception e) {
                    System.out.println("Shutdown connection");
                    conn.close();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            threadManager.shutdown();
        }
    }
    // Methods from HP
    public void close() throws IOException {
        soc.close();
    }
    @Override
    public void sendToServer(Board board, int color) throws IOException {

    }

    @Override
    public OthelloServer.Move receiveFromServer() throws IOException {
        return null;
    }

    @Override
    public String checkChars() throws IOException {
        return "";
    }
}
