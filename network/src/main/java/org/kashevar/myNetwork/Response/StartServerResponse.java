package org.kashevar.myNetwork.Response;



import java.nio.file.Path;
import java.util.List;

public class StartServerResponse implements BasicResponse {

    private List<String> listStart;

    public List<String> getListStart() {
        return listStart;
    }

    public StartServerResponse(List<String> list) {
       this.listStart = list;
    }

    @Override
    public String getType() {
        return "Start response";
    }


}
