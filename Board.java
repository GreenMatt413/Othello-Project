import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private final int[][] board;

    // Values for each board index color
    public static final int NONE = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private boolean didPlayerQuit;

    // Converts int color to string
    private static String color2String(final int c) {
        if (c == WHITE) {
            return "O";
        }
        if (c == BLACK) {
            return "X";
        }
        return "-";
    }
    // Converts color int to a byte for encoding purposes, will match
    // with each char X, 0, or -.
    public static byte colorToByte(final int color) {
        // "O"
        if (color == WHITE) {
            return 0x4F;
        }
        // "X"
        if (color == BLACK) {
            return 0x58;
        }
        // "-"
        return 0x2D;
    }
    // Supplementary reverse method, don't really need
//    public static int byteToColor(final byte b) {
//        if (b == 0x4F) {
//            return WHITE;
//        }
//        if (b == 0x58) {
//            return BLACK;
//        }
//        return NONE;
//    }
    // 8 by 8 board
    // Collect all row and column moves.
    static private final int[] allRows = {1,2,3,4,5,6,7,8};
    static private final int[] allCols = {1,2,3,4,5,6,7,8};
    static private final List<OthelloServer.Move> allMoves = product();
    // Create dimensions of board
    private static List<OthelloServer.Move> product() {
        List<OthelloServer.Move> r = new ArrayList<>();
        for ( int i : Board.allRows) {
            for ( int j : Board.allCols) {
                r.add( new OthelloServer.Move(i,j) );
            }
        }
        return r;
    }
    // Check bounds and legality of move
    public boolean isLegalMove(OthelloServer.Move move, int c) {
        if (move.isPassed() ) {
            return !isPlayable(c);
        }
        return isLegalMove(move.getRowIndex(), move.getColumnIndex(), c );
    }
    private boolean isLegalMove(int rowIndex, int columnIndex, int c) {
        // Checks index bounds for legal moves based on where stones are.
        if ( rowIndex <= 0 || rowIndex > 8 || columnIndex <= 0 || columnIndex > 8 ) return false;
        // If no stones, place chip
        if ( board[rowIndex][columnIndex] == NONE ) {
            return isEffectiveMove(rowIndex,columnIndex,c);
        }
        return false;
    }
    // Similar to isLegalMove
    private boolean isEffectiveMove(int i, int j, int c) {
        return !(flippableIndices(i,j,c).isEmpty());
    }
    // change color in move of chip
    public static int flipColor(int color) {
        return switch (color) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
            default -> NONE;
        };
    }
    /*
    Creates linked list and checks if indices on board can be
    flipped for board move.
     */
    private List<OthelloServer.Move> flippableIndices(int i, int j, int c) {
        List<OthelloServer.Move> s = new LinkedList<>();
        for ( int di = -1; di <= 1; di++ ) {
            for ( int dj = -1 ; dj <= 1; dj++ ) {
                if ( di == 0 && dj == 0 ) continue;
                flippableIndicesLine(i+di,j+dj,di,dj,c,s);
            }
        }
        return s;
    }
    // Checking indices to see if they are allowed to be used or flipped over
    private void flippableIndicesLine(int i, int j, final int di, final int dj, final int c, List<OthelloServer.Move> list) {
        List<OthelloServer.Move> s = new LinkedList<>();
        boolean flag = false;
        // original color
        final int oc = flipColor(c);
        // Signal if move is available to made at indexes.
        while (true) {
            if (board[i][j] == oc) {
                s.add( new OthelloServer.Move(i, j));
                i += di;
                j += dj;
                flag = true;
            }
            else {
                break;
            }
        }
        // Add everything to the list if it is playable
        if (!(flag && board[i][j] != c)) {
            list.addAll(s);
        }
    }
    // Is move a legal move?
    public boolean isPlayable(final int c) {
        for (OthelloServer.Move m : allMoves) {
            if (isLegalMove(m,c)) {
                return true;
            }
        }
        return false;
    }
    // End of game conditions, when no more moves can be made.
    public boolean endOfGame() {
        return !isPlayable(WHITE) && !isPlayable(BLACK);
    }

    // Check character int at each index
    public int getBoard(int i, int j) {
        return board[i][j];
    }
    // count number of stones/chips
    public int stoneCounts(int c) {
        int s = 0;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (board[i][j] == c) {
                    s += 1;
                }
            }
        }
        return s;
    }
    // Collects all legal moves using a linked list
    public List<OthelloServer.Move> legalMoves(final int c ) {
        List<OthelloServer.Move> r = new LinkedList<>();
        for ( OthelloServer.Move m : allMoves ) {
            if (isLegalMove(m,c)) {
                r.add(m);
            }
        }
        return r;
    }
    // How we define illegal moves
    public static class IllegalMoveException extends Exception {
        private final OthelloServer.Move illegalMove;

        public IllegalMoveException(OthelloServer.Move m) {
            super("IllegalMoveException");
            this.illegalMove = m;
        }

        public OthelloServer.Move getMove() {
            return illegalMove;
        }
    }
    public void move(OthelloServer.Move mv, int c) throws IllegalMoveException {
        if (mv.isPassed()) {
            if(isPlayable(c)) {
                throw new IllegalMoveException(mv);
            }
            return;
        }
        // Rows and columns of the board, check for flippable indices
        int i = mv.getRowIndex();
        int j = mv.getColumnIndex();
        List<OthelloServer.Move> moves = flippableIndices(i, j, c);
        if(moves.isEmpty()) {
            throw new IllegalMoveException(mv);
        }
        // Set each index to a color state
        for (OthelloServer.Move m : moves) {
                board[m.getRowIndex()][m.getColumnIndex()] = c;
        }
        board[i][j] = c;
        }
//        private void move(int i, int j, int c) throws IllegalMoveException {
//            move(new OthelloServer.Move(i, j), c);
//        }
    // Create board and center square
    public Board() {
        this.board = new int[8][8];
        this.board[4][4] = WHITE;
        this.board[5][5] = WHITE;
        this.board[4][5] = BLACK;
        this.board[5][4] = BLACK;
        this.didPlayerQuit = false;
    }
//    public Board(int[][] board) {
//        this();
//        for (int i = 1; i <= 8; i++) {
//            System.arraycopy(board[i], 1, this.board[i], 1, 8);
//        }
//        this.didPlayerQuit = false;
//    }
    public void print() {
        System.out.print(this);
    }
    public void print(int c) {
        System.out.print(this.toString(c));
    }

    // toString for board and stone colors
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append(" |A B C D E F G H ").append(nl);
        sb.append("-+----------------").append(nl);
        for (int i = 1; i <= 8; i++) {
            sb.append(i).append("|");
            for (int j = 1; j <= 8; j++) {
                sb.append(color2String(board[i][j])).append(" ");
            }
            sb.append(nl);
        }
        // Print number of black and white stones.
        sb.append("Black: ").append(stoneCounts(BLACK)).append(nl);
        sb.append("White: ").append(stoneCounts(WHITE)).append(nl);
        return sb.toString();
    }
    public String toString(int c) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append(" |A B C D E F G H ").append(nl);
        sb.append("-+----------------").append(nl);
        for (int i = 1; i <= 8; i++) {
            sb.append(i).append("|");
            for (int j = 1; j <= 8; j++) {
                if (board[i][j] == NONE && isLegalMove(i, j, c)) {
                    sb.append(". ");
                }
                else {
                    sb.append(color2String(board[i][j])).append(" ");
                }
            }
            sb.append( nl );
        }
        sb.append("Black: ").append(stoneCounts(BLACK)).append(nl);
        sb.append("White: ").append(stoneCounts(WHITE)).append(nl);
        return sb.toString();
    }
    // Detect if player quit
    public boolean getPlayerQuit() {
        return this.didPlayerQuit;
    }
    public void setPlayerQuit() {
        this.didPlayerQuit = true;
    }
    // Write board index bytes to output
    public void writeTo(OutputStream os) throws IOException {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                os.write(colorToByte(this.getBoard(i,j)));
            }
        }
    }
}
