package es.cmlozano.tool.pdftools.view.controller;

import es.cmlozano.tool.pdftools.utils.AlertUtils;
import es.cmlozano.tool.pdftools.utils.LayoutLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

/**
 * View Controller for main-view.xml
 *
 * @author cmlozano
 *
 */
public class MainViewController {
	@FXML
	private BorderPane borderPane;

	@FXML
	private void initialize() {
	}

	public void showMerge() {
		changeCenterLayout("merge");
	}

	public void showSplit() {
		changeCenterLayout("split");
	}

	private void changeCenterLayout(String layoutName) {
		LayoutLoader.changeLayout(layoutName, borderPane.getCenter(),  pane -> { borderPane.setCenter(pane); });
	}

    public void showAbout(ActionEvent actionEvent) {
		AlertUtils.getAlertInfo("PDF Tools version 2.1", "Developer: Carlos Manuel Lozano Soto\nRelease Date: 2023/04/19").show();
    }
}
