package es.cmlozano.tool.pdftools.view;

import java.io.IOException;
import java.io.InputStream;

import es.cmlozano.tool.pdftools.view.scene.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static final String SCENE_VIEW_FXML = "scene/View.fxml";
	private static final String APP_ICON = "app.png";
	private static final String TITLE = "Pdf Merge";
	private Stage stage;
	private VBox rootLayout;
	private FXMLLoader loader;

	public MainApp() {
	}

	public void launch() {
		Application.launch();
	}

	@Override
	public void start(final Stage stage) {
		this.stage = stage;
		this.loader = new FXMLLoader();
		try {
			this.initRootLayout();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void initRootLayout() throws IOException {
		final InputStream resourceAsStream = this.getClass().getResourceAsStream(APP_ICON);
		if (resourceAsStream != null) {
			final Image icon = new Image(resourceAsStream);
			this.stage.getIcons().add(icon);
		}

		this.loadRootLayout();
		this.declareScene();
		this.setStageExtraParameters();
		this.controllersPermissions();

	}

	private void loadRootLayout() throws IOException {
		this.loader.setLocation(this.getClass().getResource(SCENE_VIEW_FXML));
		this.rootLayout = (VBox) this.loader.load();
	}

	private void declareScene() {
		final Scene scene = new Scene(this.rootLayout);
		this.stage.setScene(scene);

	}

	private void setStageExtraParameters() {
		this.stage.setTitle(TITLE);
		this.stage.show();
		this.stage.setResizable(false);
	}

	private void controllersPermissions() {
		final ViewController controller = this.loader.getController();
		controller.setMainApp(this);
	}

	public Stage getStage() {
		return this.stage;
	}
}
