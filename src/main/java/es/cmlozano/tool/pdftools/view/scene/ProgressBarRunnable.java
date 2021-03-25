package es.cmlozano.tool.pdftools.view.scene;

import javafx.scene.control.ProgressBar;

public class ProgressBarRunnable implements Runnable {

	private final ProgressBar launchProgress;

	ProgressBarRunnable(final ProgressBar launchProgress) {
		this.launchProgress = launchProgress;
	}

	@Override
	public void run() {
		this.launchProgress.setProgress(1);
	}
}
