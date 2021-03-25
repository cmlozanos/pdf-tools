package es.cmlozano.tool.pdftools.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {

	private static final String WARNING_DIALOG = "Mensaje de Aviso";
	private static final String ERROR_DIALOG = "Mensaje de Error";
	private static final String INFO_DIALOG = "Mensaje de Información";

	public static void alertManualClose(final Alert alert) {
		alert.getDialogPane().getScene().getWindow().hide();
	}

	public static Alert showAndWaitAlertError(final String headerText, final String message) {
		return showAndWaitAlert(getAlertError(headerText, message));
	}

	public static Alert showAndWaitAlertInfo(final String headerText, final String message) {
		return showAndWaitAlert(getAlertInfo(headerText, message));
	}

	public static Alert showAlertWarning(final String headerText, final String message) {
		return showAndWaitAlert(getAlertWarning(headerText, message));
	}

	public static Alert getAlertError(final String headerText, final String message) {
		return getAlert(headerText, message, AlertType.ERROR, ERROR_DIALOG);
	}

	public static Alert getAlertInfo(final String headerText, final String message) {
		return getAlert(headerText, message, AlertType.INFORMATION, INFO_DIALOG);
	}

	public static Alert getAlertWarning(final String headerText, final String message) {
		return getAlert(headerText, message, AlertType.WARNING, WARNING_DIALOG);
	}

	private static Alert showAndWaitAlert(final Alert alert) {
		alert.showAndWait();
		return alert;
	}

	private static Alert getAlert(final String headerText, final String message, final AlertType type,
			final String title) {
		final Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(message);
		return alert;
	}

}
