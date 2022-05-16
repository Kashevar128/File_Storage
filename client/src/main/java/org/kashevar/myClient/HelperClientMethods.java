package org.kashevar.myClient;

public class HelperClientMethods {

    public static String editingPath(String path, String name) {
        int s = path.indexOf(name);
        String newPath = path.substring(s);
        return newPath;
    }
}
