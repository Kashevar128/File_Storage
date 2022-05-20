package org.kashevar.myNetwork.Response;

public class SendToFileResponse implements BasicResponse {

    @Override
    public String getType() {
        return "Начало приема файла...";
    }
}
