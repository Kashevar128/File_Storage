package org.kashevar.myClient.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import org.kashevar.myClient.clientLogic.FileInfo;
import org.kashevar.myClient.clientLogic.NettyClient;
import org.kashevar.myNetwork.HelperClasses.FileSaw;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    NettyClient nettyClient;

    String name = "Bro";

    private boolean delFile = false;

    private boolean moveFile = false;

    private PanelController srcPC = null, dstPC = null;

    Path srcPath = null, dstPath = null;

    public boolean isDelFile() {
        return delFile;
    }

    public void setDelFile(boolean delFile) {
        this.delFile = delFile;
    }

    public boolean isMoveFile() {
        return moveFile;
    }

    public void setMoveFile(boolean moveFile) {
        this.moveFile = moveFile;
    }

    @FXML
    VBox clientPanel, serverPanel;

    public PanelClientController clientPC;
    public PanelServerController serverPC;

    private boolean transfer;

    public boolean getFlagTransfer() {
        return transfer;
    }

    public void thisIsTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    @FXML
    public void exitBtnAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    public void copyBtnAction(ActionEvent actionEvent) {

        if (clientPC.getSelectedFileInfo() == null && serverPC.getSelectedFileInfo() == null) {
            AlertWindowClass.showSelectFileAlert();
            return;
        }

        if (clientPC.getSelectedFileInfo() != null) {
            srcPC = clientPC;
            dstPC = serverPC;
            thisIsTransfer(true);
        }

        if (serverPC.getSelectedFileInfo() != null) {
            srcPC = serverPC;
            dstPC = clientPC;
            thisIsTransfer(false);
        }

        FileInfo selectedFile = srcPC.getSelectedFileInfo();
        srcPath = Paths.get(srcPC.getCurrentPath(), selectedFile.getFilename());
        dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        if (srcPath.toString().equals(dstPath.toString()) || dstPath.toString().
                equals(srcPath + "\\" + selectedFile.getFilename())) {
            AlertWindowClass.showOperationError();
            return;
        }

        if (isDelFile()) {
            srcPC.delFile(srcPath);
            srcPC.updateList(Paths.get(srcPC.getCurrentPath()));
            setDelFile(false);
            return;
        }

        try {
            for (String fileName : dstPC.getStringListFiles()) {
                if (fileName.equals(srcPath.getFileName().toString())) {
                    Optional<ButtonType> option = AlertWindowClass.showTheFileAlreadyExists();
                    if (option.get() == ButtonType.OK) {
                        dstPC.delFile(dstPath);
                    } else return;
                }
            }

            Files.copy(srcPath, dstPath);
            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));

            if (isMoveFile()) {
                srcPC.delFile(srcPath);
                srcPC.updateList(Paths.get(srcPC.getCurrentPath()));
                setMoveFile(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
            AlertWindowClass.showCopyError();
        }
    }

    @FXML
    public void delBtnAction(ActionEvent actionEvent) {
        setDelFile(true);
        copyBtnAction(actionEvent);
    }

    @FXML
    public void moveFile(ActionEvent actionEvent) {
        setMoveFile(true);
        copyBtnAction(actionEvent);
    }

    @FXML
    public void refresh(ActionEvent actionEvent) {
        srcPC.updateList(Paths.get(srcPC.getCurrentPath()));
        dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            nettyClient = new NettyClient(this);
        } catch (InterruptedException e) {
            throw new RuntimeException("Проблемы при запуске клиента");
        }
        clientPC = (PanelClientController) clientPanel.getProperties().get("ctrl");
        serverPC = (PanelServerController) serverPanel.getProperties().get("ctrl");
        serverPC.setNettyClient(nettyClient);

    }

    private void copyMyFile() {
        if(transfer) {
            FileSaw.saw(srcPath, bytes -> {
                
            });
        } else {

        }
    }
}
