module client {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires network;

    exports org.kashevar.myClient.GUI;
    opens org.kashevar.myClient.GUI to javafx.fxml;
}
