
public class ClientTest {
    public static void main(String[] args) {
        // Creating a new client
        OthelloClient test = new OthelloClient();
        // Starting multiple iterations of the client.
        for (int i = 0; i < 5; i++) {
            OthelloClient.startClient("127.0.0.1", 12345, test);
        }
        // Prints how many wins, draws, and losses
        test.printCount();
        System.out.println("Game is finished.");
        // Second client
        // OthelloClient.startTwo("127.0.0.1", 12345);
    }

}
