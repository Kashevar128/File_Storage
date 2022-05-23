package org.kashevar.myNetwork.HelperClasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FileHelper {

    private static final int MB_100 = 100_000_000;

    public static void saw (Path path, Consumer<byte[]> filePartConsumer) {
        byte[] filePart = new byte[MB_100];
        try(FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
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
}
