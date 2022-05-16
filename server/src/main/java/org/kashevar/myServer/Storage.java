package org.kashevar.myServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static Map<String, Path> listUserRepositories;

    private static Storage storage;

    synchronized public static Storage getStorage() {
        if(storage == null) {
            storage = new Storage();
        }
        return storage;
    }

    synchronized public static Map<String, Path> getListUserRepositories() {
        return listUserRepositories;
    }

    Storage() {
        listUserRepositories = new HashMap<>();
    }

    synchronized public Path createUserRepository(String nameClient) {

        Path path = Paths.get( "./server/Data_Storage/" + nameClient);
        if(!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        listUserRepositories.put(nameClient, path);
        System.out.println(listUserRepositories);
        return path;
    }

}
