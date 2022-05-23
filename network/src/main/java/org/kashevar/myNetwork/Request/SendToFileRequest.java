package org.kashevar.myNetwork.Request;

public class SendToFileRequest implements BasicRequest {

    private String path;

    private byte[] file;

    private boolean isDirectory;

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getPath() {
        return path;
    }

    public byte[] getFile() {
        return file;
    }

    public SendToFileRequest(String path, byte[] file) {
        this.path = path;
        this.file = file;
        this.isDirectory = false;
    }

    @Override
    public String getType() {
        return "Передача файла...";
    }
}
