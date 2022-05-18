package org.kashevar.myNetwork.Request;

import org.kashevar.myNetwork.Response.BasicResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TransferFileRequest implements BasicResponse {

    String nameUser;
    File file;

    public String getNameUser() {
        return nameUser;
    }

    public TransferFileRequest(Path path, String nameUser) {
        this.nameUser = nameUser;

    }

    @Override
    public String getType() {
        return "file received";
    }
}
