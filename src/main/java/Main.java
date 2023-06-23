import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try (ServerSocket server = new ServerSocket(8989);) {
            while (true) {
                try (Socket client = server.accept()) {
                    PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String x = reader.readLine();
                    writer.println(gson.toJson(engine.search(x)));
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}