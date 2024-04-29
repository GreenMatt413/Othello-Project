import java.io.IOException;
interface HP {
    OthelloServer.Move play(Board board, int color) throws IOException;
    void sendToServer(Board board, int color) throws IOException;
    OthelloServer.Move receiveFromServer() throws  IOException;
    String checkChars() throws IOException;

}
