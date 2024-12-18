package pr3;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Реализовать класс UserFriend. Поля — int userId, friendId. Заполнить массив объектов UserFriend случайными данными.

//Реализовать функцию: Observable<UserFriend> getFriends(int userId), возвращающую поток объектов UserFriend, по заданному userId.
// (Для формирования потока из массива возможно использование функции Observable.fromArray(T[] arr)).

//Дан массив из случайных userId. Сформировать поток из этого массива. Преобразовать данный поток в поток объектов UserFriend.
// Обязательно получение UserFriend через функцию getFriends.

public class Task3 {
    private static UserFriend[] userFriendArray = new UserFriend[10];
    private static Integer[] userIdArray = new Integer[5];
    public static Observable<UserFriend> getFriends(int userId) {
        return Observable.fromArray(userFriendArray).filter(userFriend -> userFriend.getUserId() == userId);
    }
    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            userFriendArray[i] = new UserFriend(random.nextInt(10), random.nextInt(10));
        } //Заполнить массив объектов UserFriend случайными данными.
        for (int i = 0; i < 5; i++) {
            userIdArray[i] = random.nextInt(10);
        } //Дан массив из случайных userId.
        Observable<Integer> userIdStream = Observable.fromArray(userIdArray); //Сформировать поток из этого массива.
        userIdStream.flatMap(Task3::getFriends).subscribe(userFriend -> System.out.println(userFriend));
    }
}

class UserFriend {
    private int userId;
    private int friendId;

    public int getUserId() {
        return userId;
    }
    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    public String toString() {
        return "UserFriend: userId = " + userId + ", friendId = " + friendId;
    }
}