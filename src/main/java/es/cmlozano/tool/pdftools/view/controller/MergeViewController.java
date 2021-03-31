package es.cmlozano.tool.pdftools.view.controller;

import es.cmlozano.tool.pdftools.bean.PdfMergeUtilityRequestBean;
import es.cmlozano.tool.pdftools.service.MergeService;
import es.cmlozano.tool.pdftools.utils.AlertUtils;
import es.cmlozano.tool.pdftools.utils.FileUtils;
import es.cmlozano.tool.pdftools.utils.ViewItemsController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class MergeViewController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField fileCount;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField folder;

    @FXML
    private ProgressBar launchProgress;

    private static final String PDF_EXTENSION = "pdf";
    private static final List<String> VALID_EXTENSIONS = List.of(PDF_EXTENSION);
    private static final String LANZADO_PROCESO_DE_UNION = "Lanzado proceso de unión";
    private static final String ESPERE_HASTA_LA_FINALIZACION_DEL_PROCESO = "Espere hasta la finalización del proceso";
    private static final String ES_NECESARIA_MAS_INFORMACION = "Es necesaria más información";
    private static final String THERE_IS_NO_FOLDER_SELECTED = "No hay ninguna carpeta seleccionada";
    private static final String THERE_IS_NO_FILES_SELECTED = "No hay ficheros seleccionados";
    private static final String THE_FILE_WAS_NO_CREATED = "El fichero no ha sido creado";
    private static final String THE_FILE_WAS_SUCESSFULLY_CREATED = "El fichero ha sido creado";
    private static final String PROGRESS_COMPLETE = "Progreso completado";
    private static final String PDF_FILES = "Ficheros pdf";


    private final MergeService controller = new MergeService();
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    private void initialize() {
        this.clear();
        this.setFileChooserExtensionFilter();
        ViewItemsController.setDragAndDropEventsForList(this.listView, (DragEvent event, List<File> files) -> {
            if (FileUtils.validateExtensions(files, VALID_EXTENSIONS)) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        }, this::updateListAndFileCount);

    }

    public void selectFiles() {
        this.clearFiles();

        final List<File> files = this.fileChooser.showOpenMultipleDialog(borderPane.getScene().getWindow());
        if (null != files) {
            this.updateListAndFileCount(files);
        }
    }

    public void clearFiles() {
        this.clearList();
        ViewItemsController.startProgress(this.launchProgress);
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

        this.launchProgress.setProgress(0.2);
        final PdfMergeUtilityRequestBean pdfMergerUtilityRequestBean = new PdfMergeUtilityRequestBean(
                destinationFileName, this.listView.getItems());

        this.launchProgress.setProgress(0.3);
        final boolean launchMergeResult = this.controller.run(pdfMergerUtilityRequestBean);

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

        boolean filesSelectedEmpty = false;
        boolean folderSelectedEmpty = false;

        if (ViewItemsController.isNumberTextFieldValueZero(this.fileCount)) {
            message.append(THERE_IS_NO_FILES_SELECTED);
            filesSelectedEmpty = true;
        }

        if (ViewItemsController.isStringTextFieldEmpty(this.folder)) {
            final String prefix = filesSelectedEmpty ? "\n" : "";
            message.append(prefix).append(THERE_IS_NO_FOLDER_SELECTED);
            folderSelectedEmpty = true;
        }

        if (filesSelectedEmpty || folderSelectedEmpty) {
            AlertUtils.showAlertWarning(ES_NECESARIA_MAS_INFORMACION, message.toString());
            return false;
        }
        return true;
    }

    private void setFileChooserExtensionFilter() {
        VALID_EXTENSIONS.forEach(extension -> this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(PDF_FILES, "*." + extension)));
    }

    private void updateListAndFileCount(List<File> files) {
        ViewItemsController.updateListWithFilesPath(this.listView, files);
        ViewItemsController.setNumberTextField(this.fileCount, listView.getItems().size());
    }

    private void clear() {
        this.clearList();

        this.clearFolder();
        ViewItemsController.startProgress(this.launchProgress);
    }

    private void clearList() {
        ViewItemsController.clearList(this.listView);
        ViewItemsController.clearNumberTextField(this.fileCount);
    }

    private void clearFolder() {
        ViewItemsController.clearStringTextField(this.folder);
    }
}
