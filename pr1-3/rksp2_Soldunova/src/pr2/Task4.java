package pr2;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Task4 {
    private static Map<Path, List<String>> fileContentsMap = new HashMap<>();
    private static Map<String, Short> fileSums = new HashMap<>();
    public static void main(String[] args) throws IOException, InterruptedException {
        Path directory = Paths.get("./src/pr2"); // Наблюдаем здесь
        WatchService watchService = FileSystems.getDefault().newWatchService();
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
// При создании нового файла выводим его название
                    Path filePath = (Path) event.context();
                    System.out.println("Создан новый файл: " + filePath);
                    fileContentsMap.put(filePath, readLinesFromFile(directory.resolve(filePath)));
                    calculateChecksum(filePath.toString());
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
// При изменении файла выводим список изменений
                    Path filePath = (Path) event.context();
                    System.out.println("Файл изменен: " + filePath);
                    detectFileChanges(directory.resolve(filePath));
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    Path filePath = (Path) event.context();
                    System.out.println("Удален файл: " + filePath);
                    short sum = fileSums.get("./src/pr2/"+filePath);
                    System.out.printf("Контрольная сумма файла %s: 0x%04X%n", filePath, sum);
                }

            }
            key.reset();
        }
    }
    public static void calculateChecksum(String filePath) throws IOException {
        filePath = "./src/pr2/" + filePath;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             FileChannel fileChannel = fileInputStream.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2); // Создаем буфер для хранения 2 байт
            short checksum = 0;
            while (fileChannel.read(buffer) != -1) {
                buffer.flip(); // Переключаем буфер в режим чтения
                while (buffer.hasRemaining()) {
                    checksum ^= buffer.get(); // Выполняем XOR над байтами
                }
                buffer.clear(); // Очищаем буфер для следующего чтения
            }
            fileSums.put(filePath,checksum);
        }
    }
    private static void detectFileChanges(Path filePath) throws IOException {
        List<String> newFileContents = readLinesFromFile(filePath);
        List<String> oldFileContents = fileContentsMap.get(filePath);
        if (oldFileContents != null) {
            List<String> addedLines = newFileContents.stream()
                    .filter(line -> !oldFileContents.contains(line))
                    .toList();
            List<String> deletedLines = oldFileContents.stream()
                    .filter(line -> !newFileContents.contains(line))
                    .toList();
            if (!addedLines.isEmpty()) {
                System.out.println("Добавленные строки в файле " + filePath + ":");
                addedLines.forEach(line -> System.out.println("+ " + line));
            }
            if (!deletedLines.isEmpty()) {
                System.out.println("Удаленные строки из файла " + filePath + ":");
                deletedLines.forEach(line -> System.out.println("- " + line));
            }
        }
        calculateChecksum(filePath.toString().substring(9));
// Обновляем хранимое содержимое файла
        fileContentsMap.put(filePath, newFileContents);
    }
    private static List<String> readLinesFromFile(Path filePath) throws IOException, FileSystemException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
