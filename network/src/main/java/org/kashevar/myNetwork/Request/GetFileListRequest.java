package org.kashevar.myNetwork.Request;

import java.nio.file.Path;

public class GetFileListRequest implements BasicRequest {

    private String path;

    public String getPath() {
        return path;
    }

    public GetFileListRequest(String path) {
        this.path = path.toString();
    }

    @Override
    public String getType() {
        return "Get file list...";
    }


}
