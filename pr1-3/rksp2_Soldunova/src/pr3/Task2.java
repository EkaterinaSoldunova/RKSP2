package pr3;

import io.reactivex.Observable;

import java.util.Random;

public class Task2 {
    public static Observable<Integer> createStream(int count, int range) {
        Observable<Integer> stream = Observable.create(number -> {
                    Random random = new Random();
                    for (int i = 0; i < count; i++) {
                        number.onNext(random.nextInt(range));
                    }
                    number.onComplete();
                })
                .map(number -> (Integer) number);
        return stream;
    }
    // Преобразовать поток из 1000 случайных чисел от 0 до 1000 в поток, содержащий только числа больше 500.
    public static void FirstTask(){
        Observable<Integer> stream = createStream(1000, 1001).filter(number -> number > 500);
        stream.subscribe(number -> System.out.print(number + " "));
    }
    // Даны два потока по 1000 элементов. Каждый содержит случайные цифры. Сформировать поток, обрабатывающий оба потока последовательно.
    public static void SecondTask(){
        Observable<Integer> stream1 = createStream(1000, 10);
        Observable<Integer> stream2 = createStream(1000, 10);
        Observable<Integer> mergedStream = Observable.concat(stream1, stream2);
        mergedStream.subscribe(number -> System.out.print(number + " "));
    }
    // Дан поток из 10 случайных чисел. Сформировать поток, содержащий только первые 5 чисел.
    public static void ThirdTask(){
        Observable<Integer> stream = createStream(10, 100).take(5);
        stream.subscribe(number -> System.out.print(number + " "));
    }
    public static void main(String[] args) {
        System.out.println("Преобразовать поток из 1000 случайных чисел от 0 до 1000 в поток, содержащий только числа больше 500.");
        FirstTask();
        System.out.println("\n\nДаны два потока по 1000 элементов. Каждый содержит случайные цифры. Сформировать поток, обрабатывающий оба потока последовательно.");
        SecondTask();
        System.out.println("\n\nДан поток из 10 случайных чисел. Сформировать поток, содержащий только первые 5 чисел.");
        ThirdTask();
    }
}
