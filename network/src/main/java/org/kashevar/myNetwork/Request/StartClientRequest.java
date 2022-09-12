package org.kashevar.myNetwork.Request;

public class StartClientRequest implements BasicRequest {
    String nameUser;

    public String getNameUser() {
        return nameUser;
    }

    public StartClientRequest(String nameUser) {
        this.nameUser = nameUser;
    }

    @Override
    public String getType() {
        return "Start Request";
    }


}
