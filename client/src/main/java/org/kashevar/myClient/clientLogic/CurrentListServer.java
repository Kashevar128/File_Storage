package org.kashevar.myClient.clientLogic;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CurrentListServer {
    private List<Path> serverList;

    boolean changeFlag = false;

    public boolean isChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(boolean changeFlag) {
        this.changeFlag = changeFlag;
    }

    public List getServerList() {
        return serverList;
    }

    public void setServerList(List<Path> serverList) {
        this.serverList = serverList;
    }

    public CurrentListServer() {
        serverList = new ArrayList<>();
    }
}
