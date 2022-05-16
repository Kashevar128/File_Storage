package org.kashevar.myClient.GUI;

import org.kashevar.myClient.clientLogic.FileInfo;

import java.nio.file.Path;
import java.util.List;

public interface PanelController<T> {
    FileInfo getSelectedFileInfo();

    String getCurrentPath();

    void delFile(Path srcPath);

    void updateList(T t);

    String[] getStringListFiles();
}
