package org.kashevar.myNetwork.Request;

import org.kashevar.myNetwork.HelperClasses.FileInfo;

public class DeleteFileRequest implements BasicRequest  {
    private String path;

    private FileInfo fileInfo;

    public String getPath() {
        return path;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public DeleteFileRequest(String path, FileInfo fileInfo) {
        this.path = path;
        this.fileInfo = fileInfo;
    }

    @Override
    public String getType() {
        return "Delete file";
    }
}
