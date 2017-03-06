package chat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранение истории чата
 */
class ChatHistory {
    /**
     * Имя файла для хранения истории на диске
     */
    public final String fileName;
    /**
     * История сообщений в памяти сервера (кеш в памяти)
     */
    private final List<String> historyInMemory = new ArrayList<>();
    /**
     * Объект для хранения истории на диске
     */
    private final PrintWriter histWriter;

    ChatHistory(String chatName) throws IOException {
        fileName = chatName + ".txt";

        // Создаём объект для записи истории в файл
        Path historyFile = Paths.get(fileName);

        // Если файл существует
        if (Files.exists(historyFile)) {
            // Считываем в память всю историю чата из файла
            historyInMemory.addAll(Files.readAllLines(historyFile));
        }
        // Объект для записи истории
        histWriter = new PrintWriter(
                new FileOutputStream(historyFile.toFile(), true /* append */),
                true /* autoFlush */);
    }

    /**
     * Добавить строчку в историю
     *
     * @param line строка
     */
    synchronized void add(String line) {
        // Добавляем в локальный массив в памяти
        historyInMemory.add(line);
        // Дописываем в файл
        histWriter.println(line);
        histWriter.flush(); // Сбрасываем кеш файла на диск
    }

    /**
     * Получаем все строки начиная с заданного индекса
     *
     * @param fromIndex начиная с какого индекса?
     * @return список строк
     */
    synchronized List<String> getLines(int fromIndex) {
        List<String> list = new ArrayList<>();
        for (int i = fromIndex; i < historyInMemory.size(); i++)
            list.add(historyInMemory.get(i));
        return list;
    }
}
