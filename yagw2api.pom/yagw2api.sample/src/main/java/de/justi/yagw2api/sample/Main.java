package de.justi.yagw2api.sample;

import static com.google.common.base.Preconditions.checkState;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.sample.view.MainWindow;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	static {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

	/**
	 * @param args
	 */
	private static MainWindow mainWindow;
	private static IWVWWrapper apiWrapper;

	public static void main(String[] args) {

		try {
			checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
			apiWrapper = YAGW2APICore.getWVWWrapper();
			final IWVWAnalyzer analyzer = YAGW2APIAnalyzer.getAnalyzer();

			// start the api wrapper
			apiWrapper.start();

			apiWrapper.getAllMatches();

			// wire everything up
			apiWrapper.registerWVWMapListener(analyzer);
			apiWrapper.registerWVWMatchListener(analyzer);

			mainWindow = new MainWindow();
			// random init for now
			// mainWindow.getEternalMapModel().wireUp(apiWrapper,
			// apiWrapper.getAllMatches().iterator().next().getCenterMap());

			mainWindow.getMatchModel().wireUp(apiWrapper);
			mainWindow.setVisible(true);
		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running " + Main.class.getName() + "#main(String[])", e);
		}
	}

	public static MainWindow getMainWindow() {
		return mainWindow;
	}

	public static IWVWWrapper getWrapper() {
		return apiWrapper;
	}

}
