package org.kashevar.myNetwork.Request;

import org.kashevar.myNetwork.HelperClasses.FileInfo;

public class SendFileRequest implements BasicRequest {

    private byte[] file;

    private String dstPath;

    private FileInfo fileInfo;

    private FileInfo fileChildrenInfo;

    public byte[] getFile() {
        return file;
    }

    public String getDstPath() {
        return dstPath;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public FileInfo getFileChildrenInfo() {
        return fileChildrenInfo;
    }

    public SendFileRequest(byte[] file, String dstPath, FileInfo fileInfo) {
        this(file, dstPath, fileInfo, null);
    }

    public SendFileRequest(byte[] file, String dstPath, FileInfo fileInfo, FileInfo fileChildrenInfo) {
        this.file = file;
        this.dstPath = dstPath;
        this.fileInfo = fileInfo;
        this.fileChildrenInfo = fileChildrenInfo;
    }

    @Override
    public String getType() {
        return "Send file";
    }
}
