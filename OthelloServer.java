import java.io.*;

public class OthelloServer {
    // Move logistics
    public static class Move {
        protected int i;

        protected int j;
        protected boolean is_passed;
        // Get row index
        public int getRowIndex() {
            return i;
        }
        // Get column index
        public int getColumnIndex() {
            return j;
        }
        // If move has already been done
        public boolean isPassed() {
            return is_passed;
        }
        // Initialize row and column parameters.
        public Move(int i, int j) {
            this.i = i;
            this.j = j;
            this.is_passed = false;
        }

        // Initial center square of board
        public Move() {
        this.i = this.j = 4;
        this.is_passed = true;
        }

        // Byte is written as output character in move (idk if I need this)
//        public void writeTo(OutputStream os) throws IOException {
//            if (this.isPassed()) {
//                os.write(0x58);
//                os.write(0x58);
//            } else {
//                os.write((byte) (0x41 + (this.j - 1)));
//                os.write((byte) (0x31 + (this.i - 1)));
//            }
//        }

        public Move(InputStream is) throws IOException {
            byte[] buf = new byte[2];

            int read = 0;
            int rest = 2;
            while (rest > 0) {
                int r = is.read(buf, read, rest);
                if (r < 0)
                    throw new IOException("Input stream has ended too early");
                read += r;
                rest -= r;
            }

            // Checking the bounds of the chars in byte form in board
            if (buf[0] == 0x58 && buf[1] == 0x58) {
                this.i = this.j = 4;
                this.is_passed = true;
                // Outside squares from the center. (hex values for position)
            } else if (buf[0] >= 0x41 && buf[0] <= 0x48 &&
                    buf[1] >= 0x31 && buf[1] <= 0x38) {
                this.i = buf[1] - 0x31 + 1;
                this.j = buf[0] - 0x41 + 1;
                this.is_passed = false;
            } else {
                throw new IOException("Invalid move format.");
            }
        }
        // String form of column and row, like on chess board. (A1, H4, etc)
        @Override
        public String toString() {
            if ( this.isPassed() ) {
                return "Passed";
            }
            else {
                StringBuilder sb = new StringBuilder(2);
                sb.append( (char)( 'A' + (this.j - 1) ) );
                sb.append( (char)( '1' + (this.i - 1) ) );
                return new String(sb);
            }
        }

        }
        // Only starting server from one port in this case.
        private static void startServer(int port) {
        ServerPlayer s = null;
        try {
            s = new ServerPlayer(port);
            s.testConnection();
            s.close();
        } catch (Exception e) {
            System.out.println("Starting server failed.");
        }
        try {
            if (s != null) {
                s.close();
            }
        } catch (Exception ignored) {

        }
        }

        public static void main(String[] args) throws IOException {
            startServer(12345);
            System.out.println("Game finished.");
            // This was the code we thought of using initially for server
            // client interaction.
//            while (true) {
//                try {
//                    Socket so = se.accept();
//
//                    System.out.println("Connected to client!" + so);
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));
//                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(so.getOutputStream()));
//
//                    Thread t = new ControlClient(so, br, bw);
//
//                    t.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        }

//    class ControlClient extends Thread {
//        final BufferedReader br;
//        final BufferedWriter bw;
//        final Socket so;
//
//        public ControlClient(Socket s, BufferedReader br, BufferedWriter bw) {
//            this.so = s;
//            this.br = br;
//            this.bw = bw;
//        }
//
//        public void run() {
//
//        }
//
//    }
