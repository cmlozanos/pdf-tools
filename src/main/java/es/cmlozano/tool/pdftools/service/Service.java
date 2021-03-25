package es.cmlozano.tool.pdftools.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import es.cmlozano.tool.pdftools.bean.PdfMergeUtilityRequestBean;

public class Service {

	private static final String PDF_MERGED_FINAL_NAME = "Pdf-Combinado.pdf";

	public Service() {
	}

	/**
	 * Return true if ok or false if not finish
	 *
	 * @param requestBean
	 * @return
	 */
	public boolean launchMerge(final PdfMergeUtilityRequestBean requestBean) {

		// given
		final String destinationFileName = requestBean.getDestinationFolder() + "\\" + PDF_MERGED_FINAL_NAME;
		final List<String> sources = requestBean.getSources();

		// when
		final PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
		for (final String source : sources) {
			try {
				pdfMergerUtility.addSource(source);
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		pdfMergerUtility.setDestinationFileName(destinationFileName);
		final MemoryUsageSetting memoryUsageSettings = null;
		try {
			pdfMergerUtility.mergeDocuments(memoryUsageSettings);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// then
		return this.validateExistsFile(destinationFileName);
	}

	private boolean validateExistsFile(final String destinationFileName) {

		boolean exists;
		try {
			new File(destinationFileName);
			exists = true;
		} catch (final Exception e) {
			exists = false;
		}

		// final String prefix = exists ? "" : "un";
		// System.out.println("The file merge was " + prefix + "successfully
		// created");
		return exists;
	}

}
