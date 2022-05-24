package org.kashevar.myNetwork.HelperClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileHelper {

    private static final int MB_100 = 100_000_000;

    public static void saw(Path path, Consumer<byte[]> filePartConsumer) {
        byte[] filePart = new byte[MB_100];
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
            while (fileInputStream.read(filePart) != -1) {
                filePartConsumer.accept(filePart);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void filesWalk(Path file, Consumer<Path> pathConsumer) {
        try {
            List<Path> list = Files.list(file).collect(Collectors.toList());
            for (Path fileEntry : list) {
                System.out.println(fileEntry);
                if (Files.isDirectory(fileEntry)) {
                    filesWalk(fileEntry, pathConsumer);
                } else {
                    pathConsumer.accept(fileEntry);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readToByteFile(Path path) {
        try {
            byte[] bytesFile = new byte[(int) Files.size(path)];
            FileInputStream fileInputStream  = new FileInputStream(path.toFile());
            fileInputStream.read(bytesFile);
            return bytesFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeBytesToFile(Path path, byte[] file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
            fileOutputStream.write(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
