package org.kashevar.myNetwork.requests;

import java.io.Serializable;

public interface Message extends Serializable {

    String getType();

    String getMsg();
}
