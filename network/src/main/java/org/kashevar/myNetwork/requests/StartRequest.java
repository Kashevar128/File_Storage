package org.kashevar.myNetwork.requests;

public class StartRequest implements Message {

    private String msg;

    public StartRequest(String msg) {
        this.msg = msg;
    }

    @Override
    public String getType() {
        return "startReq";
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
