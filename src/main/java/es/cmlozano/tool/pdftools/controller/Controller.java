package es.cmlozano.tool.pdftools.controller;

import es.cmlozano.tool.pdftools.bean.PdfMergeUtilityRequestBean;
import es.cmlozano.tool.pdftools.service.Service;

public class Controller {

	private final Service service = new Service();

	public Controller() {
	}

	public boolean launchMerge(final PdfMergeUtilityRequestBean pdfMergerUtilityRequestBean) {
		return this.service.launchMerge(pdfMergerUtilityRequestBean);
	}

}
