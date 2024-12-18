package pr1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

//Дан массив из 10000 элементов.
//Необходимо написать несколько реализаций некоторой функции F в зависимости от варианта.
//Функция должна быть реализована следующими способами:
//1.Последовательно.
//2.С использованием многопоточности (Thread, Future, и т. д.).
//3.С использованием ForkJoin.
//После каждой операции с элементом массива (сравнение, сложение) добавить задержку в 1 мс при помощи Thread.sleep(1);
//Провести сравнительный анализ затрат по времени и памяти при запуске каждого из вариантов реализации.
//Варианты функций (выбор варианта осуществляется по формуле «Номер в списке группы % 3»)
//1.Поиск суммы элементов массива.

public class Task1_SumArray {
    private static List<Integer> arr = new ArrayList<>();
    public static void createArr(){
        Random random = new Random();
        for (int i=0; i<10000; i++) {
            arr.add(random.nextInt(100));
        }
    }

    // последовательный способ
    public static int firstWay() throws InterruptedException{
        int sum = 0;
        for (int i : arr) {
            sum += i;
            Thread.sleep(1);
        }
        return sum;
    }
    // использование многопоточности
    public static int secondWay() throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Callable<Integer>> tasks = new ArrayList<>();

        // Разделение массив на подмассивы, которые будут обрабатываться разными потоками
        int arrSize = 10000 / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * arrSize;
            int endIndex;
            if (i == numThreads - 1)
                endIndex = 9999;
            else endIndex = (i + 1) * arrSize - 1;
            List<Integer> subArray = arr.subList(startIndex, endIndex + 1); // создаем подмассив

            // Создаем задачу Callable, которая будет суммировать элементы подмассива и возвращать результат
            Callable<Integer> task = () -> {
                int sum = 0;
                for (int num : subArray) {
                    sum += num;
                    Thread.sleep(1);
                }
                return sum;
            };
            tasks.add(task);
        }
        List<Future<Integer>> futures = executor.invokeAll(tasks);

        // Получаем результаты из Future объектов и суммируем их
        int totalSum = 0;
        for (Future<Integer> future : futures) {
            totalSum += future.get();
            Thread.sleep(1);
        }
        executor.shutdown();
        return totalSum;
    }
    // использование ForkJoin
    public static int thirdWay() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new MyFork(arr));
    }

    static class MyFork extends RecursiveTask<Integer>{
        private List<Integer> arr;
        public MyFork(List<Integer> arr) {
            this.arr = arr;
        }
        @Override
        protected Integer compute(){
            if (arr.size() <= 100) {
                int sum = 0;
                for (int i : arr) {
                    sum += i;
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return sum;
            }
            else {
                MyFork firstHalf = new MyFork(arr.subList(0,arr.size()/2));
                MyFork secondHalf = new MyFork(arr.subList(arr.size()/2,arr.size()));
                firstHalf.fork();
                //secondHalf.fork();
                int a = secondHalf.compute();
                int b = firstHalf.join();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return a + b;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        createArr();

        System.out.println("Первый способ");
        long startTime = System.nanoTime();
        System.out.println("Результат: " + firstWay());
        long endTime = System.nanoTime();
        System.out.println("Время выполнения в миллисекундах: " + (endTime - startTime) / 1_000_000);
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Память: " + usedBytes/8/1024 + " Мб" + "\n");

        System.out.println("Второй способ");
        startTime = System.nanoTime();
        System.out.println("Результат: " + secondWay());
        endTime = System.nanoTime();
        System.out.println("Время выполнения в миллисекундах: " + (endTime - startTime) / 1_000_000);
        usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Память: " + usedBytes/8/1024 + " Мб" + "\n");

        System.out.println("Третий способ");
        startTime = System.nanoTime();
        System.out.println("Результат: " + thirdWay());
        endTime = System.nanoTime();
        System.out.println("Время выполнения в миллисекундах: " + (endTime - startTime) / 1_000_000);
        usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Память: " + usedBytes/8/1024 + " Мб" + "\n");
    }
}