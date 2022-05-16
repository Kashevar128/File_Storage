package org.kashevar.myNetwork.Request;

import java.nio.file.Path;

public class GetFileListRequest implements BasicRequest {

    private String path;

    private String name;

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public GetFileListRequest(Path path, String name) {
        this.path = path.toString();
        this.name = name;
    }

    @Override
    public String getType() {
        return "Get file list...";
    }


}
