package org.kashevar.myNetwork.HelperClasses;

public class EditingPath {

    public static String editing (String path, String name) {
        int s = path.indexOf(name);
        String newPath = path.substring(s);
        return newPath;
    }
}
