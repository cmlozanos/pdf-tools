package es.cmlozano.tool.pdftools.utils.interfaces;

import javafx.scene.input.DragEvent;

import java.io.File;
import java.util.List;

@FunctionalInterface
public interface HasFilesOnDragOver {
    void apply(DragEvent event, List<File> files);
}
