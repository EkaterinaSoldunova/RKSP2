package pr2;

//Создать файл формата .txt, содержащий несколько строк текста.
//С помощью пакета java.nio нужно прочитать содержимое файла и вывести данные в стандартный поток вывода.

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Task1 {
    public static void main(String[] args) throws IOException {
        String[] lines = {
                "First Line",
                "Second Line",
        };

        Path filePath = Paths.get("file.txt");
        Files.write(filePath, List.of(lines));

        List<String> fileLines = Files.readAllLines(filePath);
        System.out.println("Содержимое файла:");
        for (String line : fileLines) {
            System.out.println(line);
        }
    }
}
