package org.kashevar.myNetwork.HelperClasses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenerateList {

    public static List<String> generate (Path path) {
        String pathString = path.toString();

        List<String> list;
        try {
            list = Files.list(path).map(Objects::toString).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        list.add(pathString);
        return list;
    }

}
