package es.cmlozano.tool.pdftools.service;

import es.cmlozano.tool.pdftools.bean.PdfMergeUtilityRequestBean;
import es.cmlozano.tool.pdftools.utils.FileUtils;
import es.cmlozano.tool.pdftools.utils.PathUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MergeService {

	private static final String PDF_EXTENSION = "pdf";
	private static final String PDF_MERGED_FINAL_NAME = "Pdf-Combinado"+"." + PDF_EXTENSION;
	private static final String DIRECTORY_SEPARATOR = "\\";

	/**
	 * Return true if ok or false if not finish
	 *
	 * @param requestBean
	 * @return
	 */
	public boolean run(final PdfMergeUtilityRequestBean requestBean) {
		final PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

		var fileNames = new ArrayList<String>();
		for (final String source : requestBean.getSources()) {
			try {
				pdfMergerUtility.addSource(source);
				fileNames.add(PathUtils.getName(source, PDF_EXTENSION));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		pdfMergerUtility.setDestinationFileName(getDestinationFileName(requestBean, fileNames));

		try {
			pdfMergerUtility.mergeDocuments(null);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return FileUtils.validateExistsFile(pdfMergerUtility.getDestinationFileName());
	}

	private String getDestinationFileName(PdfMergeUtilityRequestBean requestBean, ArrayList<String> fileNames) {
		return requestBean.getDestinationFolder() + DIRECTORY_SEPARATOR + fileNames.stream().collect(Collectors.joining("-")) + "." + PDF_EXTENSION;
	}

}
