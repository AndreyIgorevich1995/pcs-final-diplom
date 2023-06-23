import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("localhost", 8989);) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer.println("бизнес");
            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
