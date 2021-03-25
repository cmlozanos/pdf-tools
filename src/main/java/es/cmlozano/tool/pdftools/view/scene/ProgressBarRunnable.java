package es.cmlozano.tool.pdftools.view.scene;

import javafx.scene.control.ProgressBar;

public class ProgressBarRunnable implements Runnable {

	private final ProgressBar launchProgress;

	ProgressBarRunnable(final ProgressBar launchProgress) {
		System.out.println("Declarado ejecutor");
		this.launchProgress = launchProgress;
	}

	@Override
	public void run() {
		System.out.println("Lanzando ejecutor");
		this.launchProgress.setProgress(1);
	}
}
