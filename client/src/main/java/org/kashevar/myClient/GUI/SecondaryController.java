package org.kashevar.myClient.GUI;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("/org/kashevar/myClient/primary");
    }
}