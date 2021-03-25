package es.cmlozano.tool.pdftools.view.scene;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.cmlozano.tool.pdftools.bean.PdfMergeUtilityRequestBean;
import es.cmlozano.tool.pdftools.controller.Controller;
import es.cmlozano.tool.pdftools.utils.AlertUtils;
import es.cmlozano.tool.pdftools.view.MainApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * View Controller for scene.xml
 *
 * @author cmlozano
 *
 */
public class ViewController {

	private static final String PDF_EXTENSION = "pdf";
	private static final List<String> VALID_EXTENSIONS = Arrays.asList(PDF_EXTENSION);

	private static final String LANZADO_PROCESO_DE_UNIÓN = "Lanzado proceso de unión";
	private static final String ESPERE_HASTA_LA_FINALIZACIÓN_DEL_PROCESO = "Espere hasta la finalización del proceso";
	private static final String IT_S_NECCESARY_MORE_INFORMATION = "Es necesaria más información";
	private static final String THERE_IS_NO_FOLDER_SELECTED = "No hay ninguna carpeta seleccionada";
	private static final String THERE_IS_NO_FILES_SELECTED = "No hay ficheros seleccionados";
	private static final String THE_FILE_WAS_NO_CREATED = "El fichero no ha sido creado";
	private static final String THE_FILE_WAS_SUCESSFULLY_CREATED = "El fichero ha sido creado";
	private static final String PROGRESS_COMPLETE = "Progreso completado";
	private static final String PDF_FILES = "Ficheros pdf";

	private MainApp mainApp;

	@FXML
	private TextField fileCount;

	@FXML
	private ListView<String> list;

	@FXML
	private TextField folder;

	@FXML
	private ProgressBar launchProgress;

	private final Controller controller = new Controller();
	private final FileChooser fileChooser = new FileChooser();
	private final DirectoryChooser directoryChooser = new DirectoryChooser();

	public ViewController() {
		super();
		// System.out.println("Initialized Controller: " +
		// this.getClass().getSimpleName());
	}

	@FXML
	private void initialize() {
		// System.out.println("Initialize Method");
		this.clear();

		this.fileChooser.getExtensionFilters().add(new ExtensionFilter(PDF_FILES, "*." + PDF_EXTENSION));

		this.list.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					if (ViewController.this.validateAllFilesExtensions(db)) {
						event.acceptTransferModes(TransferMode.COPY);
					}
				} else {
					event.consume();
				}
			}
		});

		this.list.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(final DragEvent event) {
				final Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					success = true;
					for (final File file : db.getFiles()) {
						final String absolutePath = file.getAbsolutePath();
						ViewController.this.list.getItems().add(absolutePath);
						System.out.println(absolutePath);
					}
					ViewController.this.fileCount.setText(String.valueOf(ViewController.this.list.getItems().size()));
				}
				event.setDropCompleted(success);
				event.consume();
			}

		});

	}

	private boolean validateAllFilesExtensions(final Dragboard db) {
		return VALID_EXTENSIONS.containsAll(db.getFiles().stream()
				.map(t -> ViewController.this.getExtension(t.getName())).collect(Collectors.toList()));
	}

	// Method to to get extension of a file
	private String getExtension(final String fileName) {
		final String extension = "";

		final int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) {
			return fileName.substring(i + 1).toLowerCase();
		}

		return extension;
	}

	public void selectFiles() {
		// System.out.println("selectFiles Method");
		this.clearFiles();

		final List<File> fileList = this.fileChooser.showOpenMultipleDialog(this.getStage());
		if (fileList != null) {
			for (final File file : fileList) {
				final String absolutePath = file.getAbsolutePath();
				this.list.getItems().add(absolutePath);
			}
			this.fileCount.setText(String.valueOf(this.list.getItems().size()));
		}
	}

	public void clearFiles() {
		this.clearList();
		this.clearFilesCount();
		this.clearProgress();
	}

	public void selectFolder() {
		// System.out.println("selectFolder Method");
		this.clearFolder();
		this.clearProgress();

		final File file = this.directoryChooser.showDialog(this.getStage());

		if (file != null && file.isDirectory()) {
			final String absolutePath = file.getAbsolutePath();
			this.folder.setText(absolutePath);
		}
	}

	public boolean launch() {
		// System.out.println("launch Method");
		this.clearProgress();
		if (!this.validateInputParams()) {
			return false;
		}

		final Alert alert = this.showBlockAlert();

		this.launchProgress.setProgress(0.1);
		// final long startTime = System.currentTimeMillis(); //
		// System.out.println("Initial Time: " + new Date(startTime));
		final String destinationFileName = this.folder.getText();

		this.launchProgress.setProgress(0.2);
		final PdfMergeUtilityRequestBean pdfMergerUtilityRequestBean = new PdfMergeUtilityRequestBean(
				destinationFileName, this.list.getItems());

		this.launchProgress.setProgress(0.3);
		final boolean launchMergeResult = this.controller.launchMerge(pdfMergerUtilityRequestBean);
		this.launchProgress.setProgress(0.6);

		// final long endTime = System.currentTimeMillis();
		// System.out.println("End Time: " + new Date(endTime));
		// System.out.println("Time spent: " + ((endTime - startTime) / 1000) +
		// "seconds");

		this.launchProgress.setProgress(1);

		AlertUtils.alertManualClose(alert);

		if (launchMergeResult) {
			AlertUtils.showAndWaitAlertInfo(PROGRESS_COMPLETE, THE_FILE_WAS_SUCESSFULLY_CREATED);
			return true;
		}

		AlertUtils.showAndWaitAlertError(PROGRESS_COMPLETE, THE_FILE_WAS_NO_CREATED);
		return false;
	}

	public Alert showBlockAlert() {

		final Alert alert = AlertUtils.getAlertInfo(ESPERE_HASTA_LA_FINALIZACIÓN_DEL_PROCESO, LANZADO_PROCESO_DE_UNIÓN);
		alert.getButtonTypes().clear();
		alert.show();
		return alert;
	}

	private boolean validateInputParams() {
		final StringBuilder message = new StringBuilder();

		boolean filesSelectedEmpty = false;
		boolean folderSelectedEmpty = false;

		if (this.fileCount == null || this.fileCount.getText().isEmpty()
				|| this.fileCount.getText().equalsIgnoreCase("0")) {
			message.append(THERE_IS_NO_FILES_SELECTED);
			filesSelectedEmpty = true;
		}

		if (this.folder == null || this.folder.getText().isEmpty()) {
			final String prefix = filesSelectedEmpty ? "\n" : "";
			message.append(prefix + THERE_IS_NO_FOLDER_SELECTED);
			folderSelectedEmpty = true;
		}

		if (filesSelectedEmpty || folderSelectedEmpty) {
			final String headerText = IT_S_NECCESARY_MORE_INFORMATION;
			AlertUtils.showAlertWarning(headerText, message.toString());
			return false;
		}
		return true;
	}

	private void clear() {
		this.clearList();
		this.clearFilesCount();
		this.clearFolder();
		this.clearProgress();
	}

	private void clearFilesCount() {
		this.fileCount.setText("0");
	}

	private void clearProgress() {
		if (this.launchProgress != null) {
			this.launchProgress.setProgress(0);
		}

	}

	private void clearList() {
		if (this.list != null && this.list.getItems() != null) {
			this.list.getItems().clear();
		}
	}

	private void clearFolder() {
		if (this.folder != null) {
			this.folder.setText("");
		}
	}

	public void setMainApp(final MainApp mainApp) {
		this.mainApp = mainApp;
	}

	private Stage getStage() {
		return this.mainApp.getStage();
	}
}
