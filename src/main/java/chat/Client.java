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
    private final AtomicInteger receivedLines = new AtomicInteger();

    /**
     * @param server   к какому серверу подключаемся
     * @param nickName наш ник-нейм
     */
    public Client(String server, String nickName) throws IOException {
        this.nickName = nickName;
        Socket con = new Socket(server, Server.PORT);
        System.out.println("== Connect to \"" + server + ":" + Server.PORT + "\" with name \"" + nickName + "\" ==");
        // Открываем поток отправки на сервер
        sendToServerStream = new PrintStream(con.getOutputStream(), true);
        sendToServerStream.println("\"" + nickName + "\" connected");
        // Пока нет принятых сообщений
        receivedLines.set(0);
        // Поток, который слушает входяшие сообщения
        Thread listenThread = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    // Увеличиваем количество принятых сообщений
                    receivedLines.incrementAndGet();
                }
            } catch (IOException e) {
                System.out.println("Client " + nickName + " listening finished");
            }
        });
        // Поток не должен мешать выходу из программы
        listenThread.setDaemon(true);
        // Запускаем поток
        listenThread.start();
    }

    public static void main(String... args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: Client <nickName> [<serverName / IP>]");
            return;
        }
        // Никнейм
        String nickName = args[0];
        // Имя сервера
        String server = "localhost";
        if (args.length >= 2)
            server = args[1];

        // Подключаемся к серверу
        Client client = new Client(server, nickName);

        // Команда для выхода из программы
        final String EXIT_COMMAND = "exit";
        // Показываем помощь
        System.out.println("Type \"" + EXIT_COMMAND + "\" to exit");

        // Считываем всё что вводит пользователь и отправляем на сервер
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            // Если нам передали команду выхода => выходим
            if (message.equals(EXIT_COMMAND))
                break;
            // Отправляем сообщение на сервер
            client.send(message);
        }
    }

    /**
     * @return количество принятых строк
     */
    public int getReceivedLines() {
        return receivedLines.get();
    }

    /**
     * Отправляем сообщение
     *
     * @param message Строка сообщения
     */
    public void send(String message) throws IOException {
        sendToServerStream.println(nickName + ": " + message);
    }
}
