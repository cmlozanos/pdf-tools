package es.cmlozano.tool.pdftools.view.controller;

import es.cmlozano.tool.pdftools.bean.PdfMergeUtilityRequestBean;
import es.cmlozano.tool.pdftools.service.MergeService;
import es.cmlozano.tool.pdftools.service.SplitService;
import es.cmlozano.tool.pdftools.utils.AlertUtils;
import es.cmlozano.tool.pdftools.utils.FileUtils;
import es.cmlozano.tool.pdftools.utils.ViewItemsController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.DragEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SplitViewController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField file;

    @FXML
    private CheckBox auto;

    @FXML
    private TextField fileNames;

    @FXML
    private Button fileNamesSelector;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField folder;

    @FXML
    private ProgressBar launchProgress;

    private static final Map<String, String> VALID_NAMES_EXTENSIONS = Stream.of(new String[][] {
        { "Ficheros txt", "txt" }}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    private static final Map<String, String> VALID_EXTENSIONS = Stream.of(new String[][] {
        { "Ficheros pdf", "pdf" }}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    private static final String LANZADO_PROCESO_DE_UNION = "Lanzado proceso de unión";
    private static final String ESPERE_HASTA_LA_FINALIZACION_DEL_PROCESO = "Espere hasta la finalización del proceso";
    private static final String ES_NECESARIA_MAS_INFORMACION = "Es necesaria más información";
    private static final String THERE_IS_NO_FOLDER_SELECTED = "No hay ninguna carpeta seleccionada";
    private static final String THERE_IS_NO_FILE_NAMES_SELECTED = "No hay ningun fichero de nombres seleccionado";
    private static final String THERE_IS_NO_FILES_SELECTED = "No hay ficheros seleccionados";
    private static final String THE_FILE_WAS_NO_CREATED = "El fichero no ha sido creado";
    private static final String THE_FILE_WAS_SUCESSFULLY_CREATED = "El fichero ha sido creado";
    private static final String PROGRESS_COMPLETE = "Progreso completado";


    private final SplitService controller = new SplitService();
    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser fileNamesChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    private void initialize() {
        this.clear();
        this.setFileChooserExtensionFilter();
        this.setFileNamesChooserExtensionFilter();
        /*
        ViewItemsController.setDragAndDropEventsForList(this.listView, (DragEvent event, List<File> files) -> {
            if (FileUtils.validateExtensions(files, VALID_EXTENSIONS)) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        }, this::updateListAndFileCount);
        */
    }
/*
    public void selectFiles() {
        this.clearFiles();

        final List<File> files = this.fileChooser.showOpenMultipleDialog(borderPane.getScene().getWindow());
        if (null != files) {
            this.updateListAndFileCount(files);
        }
    }
*/

    public void selectFile(ActionEvent actionEvent) {
        ViewItemsController.startProgress(this.launchProgress);
        final List<File> files = this.fileChooser.showOpenMultipleDialog(borderPane.getScene().getWindow());
        if (null != files && files.size() >= 1) {
            files.stream().findFirst().map(File::getAbsolutePath).ifPresent(this.file::setText);
        }
    }

    public void selectFileNames(ActionEvent actionEvent) {
        this.clearList();
        ViewItemsController.startProgress(this.launchProgress);
        final List<File> files = this.fileNamesChooser.showOpenMultipleDialog(borderPane.getScene().getWindow());
        if (null != files && files.size() >= 1) {
            files.stream().findFirst().ifPresent( file -> {
                this.fileNames.setText(file.getAbsolutePath());
                try {
                    Files.readAllLines(Path.of(file.getAbsolutePath())).forEach(line -> {
                        this.listView.getItems().add(line);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        /*
         */
    }

    public void selectFolder() {
        this.clearFolder();
        ViewItemsController.startProgress(this.launchProgress);

        final File file = this.directoryChooser.showDialog(borderPane.getScene().getWindow());

        if (file != null && file.isDirectory()) {
            this.folder.setText(file.getAbsolutePath());
        }
    }

    public boolean run() {
        ViewItemsController.startProgress(this.launchProgress);
        if (!this.validateInputParams()) {
            return false;
        }

        final Alert alert = this.showBlockAlert();

        this.launchProgress.setProgress(0.1);
        final String destinationFileName = this.folder.getText();

        this.launchProgress.setProgress(0.3);
        final boolean launchMergeResult = this.controller.run(destinationFileName, this.file.getText(), this.listView.getItems(), this.auto.isSelected());
        this.launchProgress.setProgress(0.6);

        ViewItemsController.finishProgress(this.launchProgress);

        AlertUtils.alertManualClose(alert);

        if (launchMergeResult) {
            AlertUtils.showAndWaitAlertInfo(PROGRESS_COMPLETE, THE_FILE_WAS_SUCESSFULLY_CREATED);
            return true;
        }

        AlertUtils.showAndWaitAlertError(PROGRESS_COMPLETE, THE_FILE_WAS_NO_CREATED);
        return false;
    }


    public Alert showBlockAlert() {
        final Alert alert = AlertUtils.getAlertInfo(ESPERE_HASTA_LA_FINALIZACION_DEL_PROCESO, LANZADO_PROCESO_DE_UNION);
        alert.getButtonTypes().clear();
        alert.show();
        return alert;
    }

    private boolean validateInputParams() {
        final StringBuilder message = new StringBuilder();

        var filesSelectedEmpty = false;
        var folderSelectedEmpty = false;
        var fileNamesEmpty = false;

        if (ViewItemsController.isStringTextFieldEmpty(this.file)) {
            message.append(THERE_IS_NO_FILES_SELECTED);
            filesSelectedEmpty = true;
        }

        if (ViewItemsController.isStringTextFieldEmpty(this.folder)) {
            final String prefix = filesSelectedEmpty ? "\n" : "";
            message.append(prefix).append(THERE_IS_NO_FOLDER_SELECTED);
            folderSelectedEmpty = true;
        }

        if (!this.auto.isSelected() && ViewItemsController.isStringTextFieldEmpty(this.fileNames)){
            final String prefix = filesSelectedEmpty ? "\n" : "";
            message.append(prefix).append(THERE_IS_NO_FILE_NAMES_SELECTED);
            fileNamesEmpty = true;
        }

        if (filesSelectedEmpty || folderSelectedEmpty || fileNamesEmpty) {
            AlertUtils.showAlertWarning(ES_NECESARIA_MAS_INFORMACION, message.toString());
            return false;
        }


        return true;
    }

    private void setFileChooserExtensionFilter() {
        VALID_EXTENSIONS.forEach((k,v) -> this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(k, "*." + v)));
    }

    private void setFileNamesChooserExtensionFilter() {
        VALID_NAMES_EXTENSIONS.forEach((k,v) -> this.fileNamesChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(k, "*." + v)));
    }
//
//    private void updateListAndFileCount(List<File> files) {
//        ViewItemsController.updateListWithFilesPath(this.listView, files);
//        ViewItemsController.setNumberTextField(this.fileCount, listView.getItems().size());
//    }

    private void clear() {
        ViewItemsController.clearStringTextField(this.file);
        this.clearList();
        this.auto.setSelected(true);
        this.setDisable(true);
        ViewItemsController.setListRowsEditable(this.listView);
        ViewItemsController.clearStringTextField(this.folder);


        this.clearFolder();
        ViewItemsController.startProgress(this.launchProgress);
    }



    private void setDisable(boolean disable) {
        this.fileNamesSelector.setDisable(disable);
        this.fileNames.setDisable(disable);
        this.listView.setDisable(disable);
    }

    private void clearList() {
        ViewItemsController.clearList(this.listView);
        ViewItemsController.clearStringTextField(this.fileNames);
    }

    private void clearFolder() {
        ViewItemsController.clearStringTextField(this.folder);
    }


    public void unchecked(SwipeEvent swipeEvent) {
        System.out.println("Unchecked");
    }

    public void checked(SwipeEvent swipeEvent) {
        System.out.println("Checked");
    }

    public void checkAction(ActionEvent actionEvent) {
        var selected = auto.isSelected();
        this.setDisable(selected);
        System.out.println("checkAction:" + selected);
    }
}
