package pr2;

//Реализовать копирование файла размером 100 Мб 4 методами:
//1)	FileInputStream/FileOutputStream
//2)	FileChannel
//3)	Apache Commons IO
//4)	Files class
//Замерить затраты по времени и памяти и провести сравнительный анализ.

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Task2 {
    public static void main(String[] args) throws IOException {
        String sourceFile = "file.txt"; // Имя исходного файла
        String destinationFile = "copy_file.txt"; // Имя файла назначения
        createFile(sourceFile, 100);
// Метод 1: FileInputStream/FileOutputStream
        long startTime = System.nanoTime();
        long usedBytes = firstWay(sourceFile, destinationFile);
        long endTime = System.nanoTime();
        printTimeAndMemoryUsage("FileInputStream/FileOutputStream", startTime, endTime, usedBytes);
// Метод 2: FileChannel
        long startTime2 = System.currentTimeMillis();
        usedBytes = secondWay(sourceFile, destinationFile);
        long endTime2 = System.currentTimeMillis();
        printTimeAndMemoryUsage("FileChannel", startTime2, endTime2, usedBytes);
// Метод 3: Apache Commons IO
        long startTime3 = System.currentTimeMillis();
        usedBytes = thirdWay(sourceFile, destinationFile);
        long endTime3 = System.currentTimeMillis();
        printTimeAndMemoryUsage("Apache Commons IO", startTime3, endTime3, usedBytes);
// Метод 4: Files class
        long startTime4 = System.currentTimeMillis();
        usedBytes = fourthWay(sourceFile, destinationFile);
        long endTime4 = System.currentTimeMillis();
        printTimeAndMemoryUsage("Files class", startTime4, endTime4, usedBytes);
    }
    private static void createFile(String fileName, int sizeInMB) throws IOException {
        byte[] data = new byte[1024 * 1024]; // 1 МБ буфер
        FileOutputStream fos = new FileOutputStream(fileName);
        for (int i = 0; i < sizeInMB; i++) {
            fos.write(data);
        }
        fos.close();
    }
    private static long firstWay(String source, String destination) throws IOException {
        FileInputStream in = new FileInputStream(source);
        FileOutputStream out = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        return usedBytes;
    }
    private static long secondWay(String source, String destination) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel sourceChannel = fis.getChannel();
        FileChannel destinationChannel = fos.getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
        fis.close();
        fos.close();
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        return usedBytes;
    }
    private static long thirdWay(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        File destFile = new File(destination);
        FileUtils.copyFile(sourceFile, destFile);
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        return usedBytes;
    }
    private static long fourthWay(String source, String destination) throws IOException {
        Path sourcePath = Path.of(source);
        Path destinationPath = Path.of(destination);
        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        long usedBytes = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        return usedBytes;
    }
    private static void printTimeAndMemoryUsage(String method, long startTime, long endTime, long usedBytes)
    {
        System.out.println("Метод " + method + ":");
        System.out.println("Время выполнения: " + (endTime - startTime));
        System.out.println("Память: " + usedBytes/8/1024 + " Мб" + "\n");
    }
}
