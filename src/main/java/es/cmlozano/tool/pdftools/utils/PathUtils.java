package es.cmlozano.tool.pdftools.utils;

import java.nio.file.Path;
import java.util.Locale;

public class PathUtils {
    public static String getName(String source, String extension) {
        return Path.of(source).getFileName().toString().toLowerCase(Locale.ROOT).replace("." + extension, "");
    }

}
