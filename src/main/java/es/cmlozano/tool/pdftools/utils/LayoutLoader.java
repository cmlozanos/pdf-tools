package es.cmlozano.tool.pdftools.utils;

import es.cmlozano.tool.pdftools.utils.interfaces.BorderPaneCenterLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LayoutLoader {

    private static final String SCENE_MERGE_VIEW_FXML = "/scene/merge-view.fxml";
    private static final String SCENE_SPLIT_VIEW_FXML = "/scene/split-view.fxml";
    private static final String SCENE_MAIN_VIEW_FXML = "/scene/main-view.fxml";

    private final FXMLLoader loader;
    private final Stage stage;

    public LayoutLoader(Stage stage) {
        this.stage = stage;
        this.loader = new FXMLLoader();
    }

    public static void changeLayout(String layoutName, Node center, BorderPaneCenterLoader borderPaneCenterLoader){
        var sameLayout = null == center ? false : layoutName.equals(center.getId());
        if(!sameLayout){
            getLayout(layoutName).ifPresent(pane -> borderPaneCenterLoader.apply(pane));
        }
    }

    public static Optional<Pane> getLayout(String layoutName){
        switch (layoutName){
            case "merge":
                return loadLayout(SCENE_MERGE_VIEW_FXML);
            case "split":
                return loadLayout(SCENE_SPLIT_VIEW_FXML);
            default:
                return loadLayout(SCENE_MAIN_VIEW_FXML);
        }
    }

    private static Optional<Pane> loadLayout(String fxmlPath) {
        var loader = new FXMLLoader();
        loader.setLocation(LayoutLoader.class.getResource(fxmlPath));
        try {
            return Optional.of(loader.load());
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
