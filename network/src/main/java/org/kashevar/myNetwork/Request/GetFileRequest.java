package org.kashevar.myNetwork.Request;

public class GetFileRequest implements BasicRequest {
    @Override
    public String getType() {
        return "Запрос на загрузку файла";
    }

    private String path;

    public String getPath() {
        return path;
    }

    public GetFileRequest(String path) {
        this.path = path;
    }
}
