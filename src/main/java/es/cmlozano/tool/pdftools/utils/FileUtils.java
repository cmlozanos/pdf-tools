package es.cmlozano.tool.pdftools.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtils {
    public static String getExtension(String fileName) {
        final int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1) {
            return fileName.substring(i + 1).toLowerCase();
        }
        return "";
    }

    public static boolean validateExtensions(List<File> files, List<String> extensions) {
        return files.stream().map(File::getName).map(FileUtils::getExtension).filter(extensions::contains).findAny().isPresent();
    }

    public static boolean validateExistsFile(final String destinationFileName) {
        return Files.exists(Path.of(destinationFileName));
    }
}
