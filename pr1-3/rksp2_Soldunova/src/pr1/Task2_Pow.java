package pr1;

//Программа запрашивает у пользователя на вход число.
// Программа имитирует обработку запроса пользователя в виде задержки от 1 до 5 секунд выводит результат: число, возведенное в квадрат.
// В момент выполнения запроса пользователь имеет возможность отправить новый запрос. Реализовать с использованием Future.

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Task2_Pow {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Scanner in = new Scanner(System.in);
        System.out.println("Введите число, квадрат которого хотите получить. Для завершения введите пустую строку");
        while (true) {
            String line = in.nextLine();
            if (line == "") break;
            try {
                int num = Integer.parseInt(line);
                Callable<Integer> task = () -> {
                    Random random = new Random();
                    Thread.sleep(random.nextInt(4001)+1000);
                    return num*num;
                };
                Future<Integer> future = executor.submit(task);
                System.out.println("Число " + num + " в квадрате: " + future.get());
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное число.");
            }
        }
        executor.shutdown();
    }
}
