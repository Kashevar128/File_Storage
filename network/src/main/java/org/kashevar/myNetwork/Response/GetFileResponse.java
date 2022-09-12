package org.kashevar.myNetwork.Response;

import org.kashevar.myNetwork.HelperClasses.FileInfo;

public class GetFileResponse implements BasicResponse {
    private byte[] file;

    private FileInfo fileInfo;

    private FileInfo fileChildrenInfo;

    public byte[] getFile() {
        return file;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public FileInfo getFileChildrenInfo() {
        return fileChildrenInfo;
    }

    public GetFileResponse(byte[] file, FileInfo fileInfo) {
        this(file, fileInfo, null);
    }

    public GetFileResponse(byte[] file, FileInfo fileInfo, FileInfo fileChildrenInfo) {
        this.file = file;
        this.fileInfo = fileInfo;
        this.fileChildrenInfo = fileChildrenInfo;
    }

    @Override
    public String getType() {
        return "File sent";
    }
}
