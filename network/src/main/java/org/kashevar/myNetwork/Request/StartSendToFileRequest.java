package org.kashevar.myNetwork.Request;

public class StartSendToFileRequest implements BasicRequest{

    private String nameUser;

    public String getNameUser() {
        return nameUser;
    }

    public String getPath() {
        return path;
    }

    String path;

    public StartSendToFileRequest(String nameUser, String path) {
        this.nameUser = nameUser;
        this.path = path;
    }


    @Override
    public String getType() {
        return "File transfer begins...";
    }
}
