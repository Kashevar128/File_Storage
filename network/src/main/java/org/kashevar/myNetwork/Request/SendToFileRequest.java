package org.kashevar.myNetwork.Request;

public class SendToFileRequest implements BasicRequest{
    @Override
    public String getType() {
        return "������� ����� ������";
    }

    private String nameUser;

    private byte[] file;

    public String getNameUser() {
        return nameUser;
    }

    public byte[] getFile() {
        return file;
    }

    public SendToFileRequest(byte[] file, String nameUser) {
        this.file = file;
        this.nameUser = nameUser;
    }
}
