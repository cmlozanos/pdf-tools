package es.cmlozano.tool.pdftools.view.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * View Model for View Controller
 * @author cmlozano
 *
 */
public class ViewModel {
	private final IntegerProperty files;
	private final StringProperty folder;
	private final IntegerProperty progress;

	public ViewModel() {
		super();
		this.files = null;
		this.folder = null;
		this.progress = null;
	}

	public ViewModel(IntegerProperty files, StringProperty folder, IntegerProperty progress) {
		super();
		this.files = files;
		this.folder = folder;
		this.progress = progress;
	}

	public IntegerProperty getFiles() {
		return files;
	}

	public StringProperty getFolder() {
		return folder;
	}

	public IntegerProperty getProgress() {
		return progress;
	}
}
