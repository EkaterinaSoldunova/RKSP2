package pr3;

//Файл. Имеет следующие характеристики:
//0. Тип файла (например XML, JSON, XLS)
//1. Размер файла — целочисленное значение от 10 до 100.

//Генератор файлов — генерирует файлы с задержкой от 100 до 1000 мс.
//Очередь — получает файлы из генератора. Вместимость очереди — 5 файлов.

//Обработчик файлов — получает файл из очереди. Каждый обработчик имеет параметр — тип файла, который он может обработать.
// Время обработки файла: «Размер файла*7мс». Система должна быть реализована при помощи инструментов RxJava.

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;

public class Task4 {
    public static void main(String[] args) throws InterruptedException {
        FileQueue fileQueue = new FileQueue();
        String[] fileTypes = {"XML", "JSON", "XLS"};
        for (String fileType : fileTypes) {
            new FileProcessor(fileType)
                    .processFiles(fileQueue.getFileObservable())
                    .subscribe();
        }
        Thread.sleep(10000); // Время выполнения программы
    }
}
// Генератор файлов
class FileGenerator {
    public Observable<File> generateFile() {
        return Observable
                .fromCallable(() -> {
                        Random random = new Random();
                        String[] fileTypes = {"XML", "JSON", "XLS"};
                        String fileType = fileTypes[random.nextInt(fileTypes.length)];
                        int fileSize = random.nextInt(91) + 10;
                        Thread.sleep(random.nextInt(901) + 100);
                        File file = new File(fileType, fileSize);
                        System.out.println("Сгенерирован файл: " + file.getType() + " размер: " + file.getSize());
                        return file;
                })
                .repeat() // Повторяем бесконечно
                .subscribeOn(Schedulers.io()) // Выполняется в фоновом потоке
                .observeOn(Schedulers.io()); // Результаты наблюдаются в фоновом потоке
    }
}
// Очередь файлов
class FileQueue {
    private final Observable<File> fileObservable;
    // Создает очередь с заданной вместимостью и подключается к генератору файлов
    public FileQueue() {
        this.fileObservable = new FileGenerator().generateFile()
                .replay(5) // Буферизирует источник файлов с ограниченной емкостью 5
                .autoConnect(); // Подключается автоматически к буферизированному источнику
    }
    // Получает наблюдаемый поток файлов
    public Observable<File> getFileObservable() {
        return fileObservable;
    }
}

// Обработчик файлов
class FileProcessor {
    private final String fileType;
    // Создает обработчик файлов для определенного типа файлов
    public FileProcessor(String fileType) {
        this.fileType = fileType;
    }
    // Обрабатывает файлы асинхронно с задержкой
    public Completable processFiles(Observable<File> fileObservable) {
        return fileObservable
                .filter(file -> file.getType().equals(fileType)) // Фильтрует файлы по типу
                .flatMapCompletable(file -> {
                    return Completable
                            .fromAction(() -> {
                                System.out.println("Обрабатывается файл: " + file.getType() + " размер: " + file.getSize());
                                Thread.sleep(file.getSize() * 7); // Имитация обработки файла
                                System.out.println("Файл обработан: " + file.getType() + " размер: " + file.getSize());
                            })
                            .subscribeOn(Schedulers.io()) // Выполняется в фоновом потоке
                            .observeOn(Schedulers.io()); // Результаты наблюдаются в фоновом потоке
                })
                .onErrorComplete(); // Игнорирует ошибки и завершает успешно
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