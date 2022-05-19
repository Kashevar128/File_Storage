package org.kashevar.myNetwork.HelperClasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileSaw {

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
}
