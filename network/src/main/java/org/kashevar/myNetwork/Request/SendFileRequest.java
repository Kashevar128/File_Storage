package org.kashevar.myNetwork.Request;

import org.kashevar.myNetwork.HelperClasses.FileInfo;

import java.nio.file.Path;

public class SendFileRequest implements BasicRequest {

    private byte[] file;

    private String dstPath;

    private FileInfo fileInfo;

    private FileInfo directoryInfo;

    public byte[] getFile() {
        return file;
    }

    public String getDstPath() {
        return dstPath;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public FileInfo getDirectoryInfo() {
        return directoryInfo;
    }

    public SendFileRequest(byte[] file, String dstPath, FileInfo fileInfo) {
        this(file, dstPath, fileInfo, null);
    }

    public SendFileRequest(byte[] file, String dstPath, FileInfo fileInfo, FileInfo directoryInfo) {
        this.file = file;
        this.dstPath = dstPath;
        this.fileInfo = fileInfo;
        this.directoryInfo = directoryInfo;
    }

    @Override
    public String getType() {
        return "Send file";
    }
}
