package es.cmlozano.tool.pdftools.utils.interfaces;

import java.io.File;
import java.util.List;

@FunctionalInterface
public interface HasFilesOnDragDropped{

    void apply(List<File> files);

}
