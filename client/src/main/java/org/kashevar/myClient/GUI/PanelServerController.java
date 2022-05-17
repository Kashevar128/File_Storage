package org.kashevar.myClient.GUI;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.kashevar.myClient.HelperClientMethods;
import org.kashevar.myClient.clientLogic.FileInfo;
import org.kashevar.myClient.clientLogic.NettyClient;
import org.kashevar.myNetwork.Request.GetFileListRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PanelServerController implements Initializable, PanelController<List<String>> {

    String stringCurrentPath;

    NettyClient nettyClient;

    public void setNettyClient(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public void setStringCurrentPath(String stringCurrentPath) {
        this.stringCurrentPath = stringCurrentPath;
    }

    public String getStringCurrentPath() {
        return stringCurrentPath;
    }

    @FXML
    public TableView<FileInfo> filesTable;

    @FXML
    TextField pathField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn<FileInfo, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfo, String> fileNameColumn = new TableColumn<>("Имя");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFilename()));
        fileNameColumn.setPrefWidth(140);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);

        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfo, String> fileDateColumn = new TableColumn<>("Дата изменения");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dft)));
        fileDateColumn.setPrefWidth(120);

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        filesTable.getSortOrder().add(fileTypeColumn);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(getStringCurrentPath()).resolve(filesTable.getSelectionModel().getSelectedItem().getFilename());
                    if (Files.isDirectory(path)) {
                        nettyClient.sendMessage(new GetFileListRequest(path, nettyClient.getNameUser()));
                    }
                }
            }
        });
    }

    public void btnPathBack(ActionEvent actionEvent) {
        Path backPath = Paths.get(getStringCurrentPath()).getParent();
        if (backPath != null && !backPath.toString().endsWith("Data_Storage")) {
                nettyClient.sendMessage(new GetFileListRequest(backPath, nettyClient.getNameUser()));
        }
    }
    @Override
    public FileInfo getSelectedFileInfo() {
        if (!filesTable.isFocused()) {
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem();
    }
    @Override
    public String getCurrentPath() {
        return pathField.getText();
    }
    @Override
    public String[] getStringListFiles() {
        String[] string = filesTable.getItems().stream().map(FileInfo::getFilename).
                collect(Collectors.toList()).toArray(String[]::new);
        return string;
    }

    @Override
    public void delFile(Path path) {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            AlertWindowClass.showDelFileError();
        }
        System.out.println("Удаленный файл или папка: " + path);
    }

    @Override
    public void updateList(List<String> list) {
        List<Path> listPath = list.stream().map(Paths::get).collect(Collectors.toList());
        Path currentPath = listPath.get(list.size() - 1);
        listPath.remove(list.size() - 1);
        pathField.setText(HelperClientMethods.editingPath(currentPath.normalize().toString(), nettyClient.getNameUser()));
        setStringCurrentPath(currentPath.normalize().toAbsolutePath().toString());
        filesTable.getItems().clear();
        filesTable.getItems().addAll(listPath.stream().map(FileInfo::new).collect(Collectors.toList()));
        filesTable.sort();
    }


}
