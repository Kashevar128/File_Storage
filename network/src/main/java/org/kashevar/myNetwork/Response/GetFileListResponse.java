package org.kashevar.myNetwork.Response;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GetFileListResponse implements BasicResponse{

    List<String> list;

    public List<String> getList() {
        return list;
    }

    public GetFileListResponse(List<String> list) {
        this.list = list;
    }

    @Override
    public String getType() {
        return "getListResponse";
    }
}
