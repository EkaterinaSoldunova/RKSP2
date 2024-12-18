package pr3;

// Реализовать следующую систему:

// Датчик температуры. Каждую секунду публикует значение температуры (случайное значение от 15 до 30).
// Датчик CO2. Каждую секунду публикует значение содержания CO2 в воздухе. (Случайное значение от 30 до 100).

// Сигнализация. Получает значения от датчиков. Если один из показателей превышает норму, выводит предупреждение об этом.
// Если норму превышаю оба показателя выводит сообщение «ALARM!!!».
// Норма показателей: Температура — 25. CO2 — 70.

// Обязательно использование классов Observer и Observable из библиотеки RxJava.

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import java.util.Random;

public class Task1 {
    public static void main(String[] args) {
        TemperatureSensor temperatureSensor = new TemperatureSensor(); // Создаем датчик температуры
        CO2Sensor co2Sensor = new CO2Sensor(); // Создаем датчик CO2
        Alarm alarm = new Alarm(); // Создаем сигнализацию

        temperatureSensor.subscribe(alarm); // Подписываем сигнализацию на датчик температуры
        co2Sensor.subscribe(alarm); // Подписываем сигнализацию на датчик CO2

        temperatureSensor.start(); // Запускаем датчик температуры
        co2Sensor.start(); // Запускаем датчик CO2
    }
}

// Класс для датчика температуры
class TemperatureSensor extends Observable<Integer> {

    private final PublishSubject<Integer> subject = PublishSubject.create();

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        subject.subscribe(observer); // Создаем подписку на события датчика температуры
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                int temperature = new Random().nextInt(15, 31); // Генерируем случайное значение температуры
                subject.onNext(temperature); // Отправляем значение температуры подписчикам
                try {
                    Thread.sleep(1000); // Пауза 1 секунда
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // Запускаем поток для симуляции работы датчика
    }
}

// Класс для датчика CO2
class CO2Sensor extends Observable<Integer> {
    private final PublishSubject<Integer> subject = PublishSubject.create();
    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        subject.subscribe(observer); // Создаем подписку на события датчика CO2
    }
    public void start() {
        new Thread(() -> {
            while (true) {
                int co2 = new Random().nextInt(31, 101); // Генерируем случайное значение CO2
                subject.onNext(co2); // Отправляем значение CO2 подписчикам
                try {
                    Thread.sleep(1000); // Пауза 1 секунда
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // Запускаем поток для симуляции работы датчика
    }
}

// Создаем класс для сигнализации
class Alarm implements Observer<Integer> {
    private int temperature = 0;
    private int co2 = 0;

    @Override
    public void onSubscribe(Disposable disposable) {
        System.out.println(disposable.hashCode() + " подписан");
    }

    @Override
    public void onNext(Integer integer) {
        if (integer <= 30){
            System.out.println("Значение от датчика температуры: " + integer);
            temperature = integer;
        }
        else {
            System.out.println("Значение от датчика CO2: " + integer);
            co2 = integer;
        }
        // Проверка на норму показателей. Температура — 25. CO2 — 70.
        if (temperature >= 25 && co2 >= 70){
            System.out.println("ALARM!!! Температура: " + temperature + ", CO2: " + co2);
        }
        else if (temperature >= 25){
            System.out.println("Температура превышает норму: " + temperature);
        }
        else if (co2 >= 70){
            System.out.println("CO2 превышает норму: " + co2);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Completed");
    }
}



