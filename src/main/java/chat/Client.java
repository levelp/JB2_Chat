package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    // Наш никнейм
    private final String nickName;
    // Поток для отправки данных серверу
    private final PrintStream sendToServerStream;

    // Количество принятых строк
    private AtomicInteger linesCounter = new AtomicInteger();

    /**
     * @param server   к какому серверу подключаемся
     * @param nickName наш ник-нейм
     */
    public Client(String server, String nickName) throws IOException {
        this.nickName = nickName;
        Socket con = new Socket(server, Server.PORT);
        System.out.println("== Connect to \"" + server + ":" + Server.PORT + "\" with name \"" + nickName + "\"");
        // Открываем поток отправки на сервер
        sendToServerStream = new PrintStream(con.getOutputStream(), true);
        // Пока нет принятых сообщений
        linesCounter.set(0);
        Thread listenThread = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    // Увеличиваем количество принятых сообщений
                    linesCounter.incrementAndGet();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client " + nickName + " listening finished");
        });
        listenThread.start();
    }

    public static void main(String... args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: Client <nickName> [<serverName / IP>]");
            return;
        }
        String nickName = args[0];
        String server = "localhost";
        if (args.length >= 2)
            server = args[1];
        System.out.println("Type \"exit\" to exit");
        Client client = new Client(server, nickName);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equals("exit"))
                break;
            client.send(message);
        }
    }

    public int getLinesCounter() {
        return linesCounter.get();
    }

    public void send(String message) throws IOException {
        sendToServerStream.println(nickName + ": " + message);
    }

}
