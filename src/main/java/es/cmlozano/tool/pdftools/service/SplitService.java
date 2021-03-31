package es.cmlozano.tool.pdftools.service;

import javafx.collections.ObservableList;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public class SplitService {

	public boolean run(String destinationFileName, String filePath, ObservableList<String> items, boolean auto) {
		try {
			var file = new File(filePath);
			var fileName = file.getName();
			var doc = PDDocument.load(file);
			var pages = new Splitter().split(doc);
			IntStream.range(0, pages.size()).forEach(i -> {
				try {
					pages.get(i).save(this.getName(destinationFileName, items, fileName, i, auto));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String getName(String destinationFileName, ObservableList<String> items, String fileName, int i, boolean auto) {
		return new StringBuilder().append(destinationFileName).append("\\").append(getFileName(items, fileName, i, auto)).append(".pdf").toString();
	}

	private String getFileName(ObservableList<String> items, String fileName, int i, boolean auto) {
		return auto ? (fileName + "." + i) : items.get(i);
	}
}
