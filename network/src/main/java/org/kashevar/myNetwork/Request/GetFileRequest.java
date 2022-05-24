package org.kashevar.myNetwork.Request;

public class GetFileRequest implements BasicRequest{

    private String srcPath;

    public String getSrcPath() {
        return srcPath;
    }

    @Override
    public String getType() {
        return "Get file";
    }
}
