package es.cmlozano.tool.pdftools.utils;

import es.cmlozano.tool.pdftools.utils.interfaces.HasFilesOnDragDropped;
import es.cmlozano.tool.pdftools.utils.interfaces.HasFilesOnDragOver;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;

import java.io.File;
import java.util.List;

public class ViewItemsController {

    /* Progress Bar */
    public static void startProgress(ProgressBar progressBar) {
        if (null != progressBar) {
            progressBar.setProgress(0);
        }
    }
    public static void finishProgress(ProgressBar progressBar) {
        if (null != progressBar) {
            progressBar.setProgress(1);
        }
    }

    /* List */
    public static void setListRowsEditable(ListView<String> list) {
        list.setCellFactory(TextFieldListCell.forListView());
    }

    public static void clearList(ListView<String> list) {
        if (null != list && null != list.getItems()) {
            list.getItems().clear();
        }
    }

    public static void updateListWithFilesPath(ListView<String> list, List<File> files) {
        files.stream().map(File::getAbsolutePath).forEach(path -> list.getItems().add(path));
    }

    public static void setDragAndDropEventsForList(ListView<String> list, HasFilesOnDragOver hasFilesOnDragOver, HasFilesOnDragDropped hasFilesOnDragDropped) {
        list.setOnDragOver(event -> {
            var db = event.getDragboard();
            if (db.hasFiles()) {
                hasFilesOnDragOver.apply(event, db.getFiles());
            } else {
                event.consume();
            }
        });

        list.setOnDragDropped(event -> {
            var db = event.getDragboard();
            var hasFiles = db.hasFiles();
            if (hasFiles) {
                hasFilesOnDragDropped.apply(db.getFiles());
            }
            event.setDropCompleted(hasFiles);
            event.consume();
        });
    }

    /* String Text Field */
    public static void clearStringTextField(TextField textField) {
        if (null != textField) {
            textField.setText("");
        }
    }

    public static boolean isStringTextFieldEmpty(TextField textField) {
        return null == textField || textField.getText().isEmpty();
    }

    /* Number Text Field */

    public static void setNumberTextField(TextField textField, int number) {
        if (null != textField) {
            textField.setText(String.valueOf(number));
        }
    }

    public static void clearNumberTextField(TextField textField) {
        if (null != textField) {
            textField.setText("0");
        }
    }

    public static boolean isNumberTextFieldValueZero(TextField textField) {
        return null == textField || textField.getText().isEmpty()
                || textField.getText().equalsIgnoreCase("0");
    }

}
