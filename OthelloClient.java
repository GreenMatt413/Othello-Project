public class OthelloClient {
    private int wins;
    private int draws;
    private int losses;

    // Changes wins by one
    public void winPlusOne() {
        this.wins++;
    }

    // Changes draws by one
    public void drawsPlusOne() {
        this.draws++;
    }

    // Changes losses by one
    public void lossesPlusOne() {
        this.losses++;
    }

    // Prints all three
    public void printCount() {
        System.out.println("Wins: " + this.wins + "- Draws: " + this.draws + "- Losses: " + this.losses);
    }

    public OthelloClient() {
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
    }

    public static void startClient(String host, int port, OthelloClient client) {
        // Creating a second player
        Player2 p = null;
        try {
            // Get player to client
             p = new Player2(host, port);
             // Create board with player attached to game
             Board b;
             b = Game.game(p);
             // print board
             b.print();
             int result = b.stoneCounts(Board.BLACK) - b.stoneCounts(Board.WHITE);
             // Compute endgame
             if (result > 0) {
             client.winPlusOne();
             } else if (result == 0) {
             client.drawsPlusOne();
             } else {
             client.lossesPlusOne();
             }
             // Say connection failed if didn't work
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection failed.");
        } try {
            if (p != null) {
                p.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void startTwo(String host, int port) {
        // Second client is made
        Player p;
        try {
            p = new Player(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
