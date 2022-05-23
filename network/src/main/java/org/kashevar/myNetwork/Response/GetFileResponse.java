package org.kashevar.myNetwork.Response;

public class GetFileResponse implements BasicResponse{
    @Override
    public String getType() {
        return "Получен файл";
    }


    private byte[] file;

    private String nameFile;


    public byte[] getFile() {
        return file;
    }

    public String getNameFile() {
        return nameFile;
    }

    public GetFileResponse(byte[] file, String nameFile) {
        this.file = file;
        this.nameFile = nameFile;
    }
}
