package org.kashevar.myNetwork.requests;

public class StartResponse implements Message {

    private String msg;

    public StartResponse(String msg) {
        this.msg = msg;
    }

    @Override
    public String getType() {
        return "startRes";
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
