package org.kashevar.myClient.GUI;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertWindowClass {

    public static void showSelectFileAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Ни один файл не был выбран", ButtonType.OK);
        alert.setHeaderText("Выберите файл");
        alert.showAndWait();
    }

    public static Optional<ButtonType> showTheFileAlreadyExists() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Файл уже существует по указанному пути",
                ButtonType.OK, ButtonType.CANCEL);
        alert.setHeaderText("Заменить файл?");
        Optional<ButtonType> option = alert.showAndWait();
        return option;
    }

    public static void showCopyError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось скопировать указанный файл", ButtonType.OK);
        alert.setHeaderText("Ошибка копирования");
        alert.showAndWait();
    }

    public static void showDelFileError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось правильно удалить файл", ButtonType.OK);
        alert.setHeaderText("Ошибка удаления");
        alert.showAndWait();
    }

    public static void showOperationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Проверьте правильность прописанных директорий", ButtonType.OK);
        alert.setHeaderText("Недопустимое действие");
        alert.showAndWait();
    }

    public static void showInformationExchangeError() {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Фатальная ошибка, программа будет закрыта", ButtonType.OK);
        alert.setHeaderText("Ошибка обмена данных");
        alert.showAndWait();
    }
}
