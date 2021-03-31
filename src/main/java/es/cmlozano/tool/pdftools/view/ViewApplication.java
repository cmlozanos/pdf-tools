package es.cmlozano.tool.pdftools.view;

import java.io.InputStream;

import es.cmlozano.tool.pdftools.utils.LayoutLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ViewApplication extends Application {


	private static final String APP_ICON = "/app.png";
	private static final String TITLE = "Pdf Tools";
	private Stage stage;

	private LayoutLoader layoutLoader;

	public void launch(){
		Application.launch();
	}

	@Override
	public void start(final Stage stage) {
		this.stage = stage;
		this.layoutLoader = new LayoutLoader(this.stage);
		this.setStageTitleAndIconAndResizable();

		LayoutLoader.getLayout("main").ifPresent(pane -> showLayout(stage, (BorderPane) pane));

		// layoutLoader.changeLayout("merge", this.stage);
		// layoutLoader.changeLayout("main");
		// LayoutLoader.getLayoutAndShowIntoStage("main", stage);

		// layoutLoader.setControllersPermissions(this);

	}

	private void showLayout(Stage stage, BorderPane pane) {
		stage.setScene(new Scene(pane));
		if (!stage.isShowing()){
			stage.show();
		}
	}

	private void setStageTitleAndIconAndResizable() {
		this.stage.setTitle(TITLE);
		final InputStream resourceAsStream = this.getClass().getResourceAsStream(APP_ICON);
		if (null != resourceAsStream) {
			final Image icon = new Image(resourceAsStream);
			this.stage.getIcons().add(icon);
		}
		// this.stage.setResizable(false);
	}

	public Stage getStage() {
		return this.stage;
	}
}
