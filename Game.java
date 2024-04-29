public class Game {
    // Player interface as parameter for game
    public static Board game(HP p1) {
        // Creates a new board
        Board board = new Board();
        // Sets chip color to black
        int c = Board.BLACK;
        while (!board.endOfGame() && !board.getPlayerQuit()) {
            try {
                // Make a move
                OthelloServer.Move m = p1.play(board, c);
                if (m == null) {
                    // If player quit, break, else, get the connection status of interface
                    if (board.getPlayerQuit()) {
                        break;
                    } else {
                    System.out.println(p1.checkChars());
                    }
                } else {
                    // If move is not taken (passed) then move is made with the color
                    if(!m.isPassed()) {
                        board.move(m, c);
                    }
                    // Flip the color
                    c = Board.flipColor(c);
                    // Send board state to server
                    p1.sendToServer(board, c);
                    // Make a new move
                    OthelloServer.Move m2 = p1.receiveFromServer();
                     System.out.println("Opponent's move: " + m2.toString());
                     // Implement move if move is not passed
                      if (!m2.isPassed()) {
                          board.move(m2, c);
                      }
                      // Flip the color with each move
                    c = Board.flipColor(c);
                }
                // Print IllegalMoveException and Exception if thrown
            } catch (Board.IllegalMoveException e) {
                board.print(c);
                System.out.println("Illegal move: " + e.getMove());
                return board;

            } catch (Exception e) {
                e.printStackTrace();
                return board;
            }
        }
        // Return board each time
        return board;
    }
}
