package es.cmlozano.tool.pdftools.bean;

import java.util.List;

public class PdfMergeUtilityRequestBean {
	private final String destinationFolder;
	private final List<String> sources;

	public PdfMergeUtilityRequestBean(final String destinationFileName, final List<String> sources) {
		super();
		this.destinationFolder = destinationFileName;
		this.sources = sources;
	}

	public String getDestinationFolder() {
		return this.destinationFolder;
	}

	public List<String> getSources() {
		return this.sources;
	}
}
