package pr1;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Task3_File {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new ArrayBlockingQueue<>(5);


        Thread generatorThread = new Thread(new FileGenerator(queue));
        generatorThread.start();


        Thread xmlHandler = new Thread(new FileHandler(queue, "XML"));
        Thread jsonHandler = new Thread(new FileHandler(queue, "JSON"));
        Thread xlsHandler = new Thread(new FileHandler(queue, "XLS"));

        xmlHandler.start();
        jsonHandler.start();
        xlsHandler.start();
    }
}
class File {
    private final String type;
    private final int size;
    public File(String type, int size) {
        this.type = type;
        this.size = size;
    }
    public String getType() {
        return type;
    }
    public int getSize() {
        return size;
    }
}
class FileGenerator implements Runnable {
    private BlockingQueue<File> queue;
    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        String[] fileTypes = {"XML", "JSON", "XLS"};
        while (true) {
            try {
                // Генерация случайного типа файла и размера
                Random random = new Random();
                String fileType = fileTypes[random.nextInt(fileTypes.length)];
                int fileSize = random.nextInt(91) + 10; // Размер от 10 до 100
                File file = new File(fileType, fileSize);

                // Добавление файла в очередь
                queue.put(file);
                System.out.println("Сгенерирован файл: " + file.getType() + " размер: " + file.getSize());

                // Задержка генерации файла
                Thread.sleep(random.nextInt(901) + 100); // Задержка от 100 до 1000 мс
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
class FileHandler implements Runnable {
    private final BlockingQueue<File> queue;
    private final String fileType;

    public FileHandler(BlockingQueue<File> queue, String fileType) {
        this.queue = queue;
        this.fileType = fileType;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Получение файла из очереди
                File file = queue.take();
                if (file.getType().equals(fileType)) {
                    // Время обработки файла
                    int processingTime = file.getSize() * 7; // Время в миллисекундах
                    System.out.println("Обрабатывается файл: " + file.getType() + " размер: " + file.getSize() + " потоком " + fileType);
                    Thread.sleep(processingTime);
                    System.out.println("Файл обработан: " + file.getType() + " размер: " + file.getSize() + " потоком " + fileType);
                } else {
                    // Если тип файла не совпадает, возвращаем его обратно в очередь
                    //System.out.println("Тип файла не подходит для обработки: " + file.getType() + " потоком " + fileType);
                    queue.put(file);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
