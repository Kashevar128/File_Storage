package org.kashevar.myClient.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import org.kashevar.myNetwork.HelperClasses.FileHelper;
import org.kashevar.myNetwork.HelperClasses.FileInfo;
import org.kashevar.myClient.clientLogic.NettyClient;
import org.kashevar.myNetwork.Request.DeleteFileRequest;
import org.kashevar.myNetwork.Request.GetFileListRequest;
import org.kashevar.myNetwork.Request.GetFileRequest;
import org.kashevar.myNetwork.Request.SendFileRequest;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    NettyClient nettyClient;

    private boolean delFile = false;

    private PanelController srcPC = null, dstPC = null;

    Path srcPath = null, dstPath = null;

    @FXML
    VBox clientPanel, serverPanel;

    public PanelClientController clientPC;
    public PanelServerController serverPC;

    private boolean transfer;

    public boolean isDelFile() {
        return delFile;
    }

    public void setDelFile(boolean delFile) {
        this.delFile = delFile;
    }

    public void thisIsTransfer(boolean transfer) {
        this.transfer = transfer;
    }

    @FXML
    public void exitBtnAction(ActionEvent actionEvent) {
        Platform.exit();
        nettyClient.exitClient();
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
            if (srcPC.getClass().equals(clientPC.getClass())) {
                srcPC.delFile(srcPath);
                srcPC.updateList(Paths.get(srcPC.getCurrentPath()));
                setDelFile(false);
                return;
            }
            if (srcPC.getClass().equals(serverPC.getClass())) {
                FileInfo fileInfo = new FileInfo(srcPath);
                nettyClient.sendMessage(new DeleteFileRequest(srcPath.toString(), fileInfo));
            }
        }

        for (String fileName : dstPC.getStringListFiles()) {
            if (fileName.equals(srcPath.getFileName().toString())) {
                Optional<ButtonType> option = AlertWindowClass.showTheFileAlreadyExists();
                if (option.get() == ButtonType.OK) {
                    dstPC.delFile(dstPath);
                } else return;
            }
        }

        copyFile();

    }

    @FXML
    public void delBtnAction(ActionEvent actionEvent) {
        setDelFile(true);
        copyBtnAction(actionEvent);
    }

    @FXML
    public void refresh(ActionEvent actionEvent) {
        clientPC.updateList(Paths.get(clientPC.getCurrentPath()));
        nettyClient.sendMessage(new GetFileListRequest(serverPC.getCurrentPath()));
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

    private void copyFile() {
        if (transfer) {
            FileInfo fileInfo = new FileInfo(srcPath);
            switch (fileInfo.getType()) {
                case FILE:
                    byte[] file = FileHelper.readToByteFile(srcPath);
                    nettyClient.sendMessage(new SendFileRequest(file, dstPath.toString(), fileInfo));
                    break;
                case DIRECTORY:
                    FileHelper.filesWalk(srcPath, (fileEntry) -> {
                        FileInfo fileInfo1 = new FileInfo(fileEntry);
                        FileInfo fileDirectory = new FileInfo(fileEntry.getParent());
                        byte[] fileChildren = FileHelper.readToByteFile(fileEntry);
                        nettyClient.sendMessage(new SendFileRequest(fileChildren, dstPath.getParent().toString(),
                                fileDirectory, fileInfo1));
                    });
            }
        }

        if (!transfer) {
            nettyClient.sendMessage(new GetFileRequest(srcPath.toString()));
        }
    }




}
