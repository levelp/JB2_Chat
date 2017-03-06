package chat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class ChatHistory {
    private final List<String> historyLocal = new ArrayList<>();

    private final PrintWriter histWriter;
    private final String fileName;

    ChatHistory(String chatName) throws IOException {
        fileName = chatName + ".txt";

        // Добавляем всю историю из файла
        Path historyFile = Paths.get(fileName);
        // Если файл существует
        if (Files.exists(historyFile)) {
            historyLocal.addAll(Files.readAllLines(historyFile));
        }
        // Объект для записи истории
        histWriter = new PrintWriter(
                new FileOutputStream(historyFile.toFile(), true /* append */),
                true /* autoFlush */);
    }

    String getFileName() {
        return fileName;
    }

    synchronized void add(String line) {
        // Сохраняем в историю
        historyLocal.add(line);
        histWriter.println(line);
        histWriter.flush();
    }

    synchronized List<String> getLines(int fromIndex) {
        List<String> list = new ArrayList<>();
        for (int i = fromIndex; i < historyLocal.size(); i++)
            list.add(historyLocal.get(i));
        return list;
    }
}
