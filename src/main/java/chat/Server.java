package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    static final int PORT = 8888;
    private final ChatHistory chatHistory;
    private final List<ClientTalker> clientTalkers = new CopyOnWriteArrayList<>();

    public Server(String chatName) throws IOException, InterruptedException {
        chatHistory = new ChatHistory(chatName);
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("ChatServer started on port " + PORT + " log \"" +
                chatHistory.getFileName() + "\"");
        try {
            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                // Общение с клиентом
                ClientTalker clientTalker = new ClientTalker(
                        clientTalkers.size() + 1,
                        clientSocket);
                clientTalkers.add(clientTalker);
                // Новый поток для общения с этим клиентом
                new Thread(clientTalker).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) throws IOException, InterruptedException {
        new Server("chat");
    }

    private synchronized void sendUpdatesToAll() {
        for (ClientTalker talker : clientTalkers) {
            talker.getUpdates();
        }
    }

    private class ClientTalker implements Runnable {
        private final int id;
        private final Socket clientSocket;
        private final PrintWriter out;
        private int linesCounter;

        ClientTalker(int id, Socket clientSocket) throws IOException {
            this.id = id;
            this.clientSocket = clientSocket;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            linesCounter = 0;
        }

        @Override
        public void run() {
            try {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("Client #" + id + ": \"" + line + "\"");
                        chatHistory.add(line);
                        sendUpdatesToAll();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        synchronized void getUpdates() {
            List<String> strings = chatHistory.getLines(linesCounter);
            linesCounter += strings.size();
            for (String s : strings) {
                out.println(s);
            }
            out.flush();
        }
    }
}
