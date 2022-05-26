package org.kashevar.myClient.GUI;

import org.kashevar.myNetwork.HelperClasses.FileInfo;

import java.nio.file.Path;

public interface PanelController<T> {
    FileInfo getSelectedFileInfo();

    String getCurrentPath();

    void delFile(Path srcPath);

    void updateList(T t);

    String[] getStringListFiles();
}
