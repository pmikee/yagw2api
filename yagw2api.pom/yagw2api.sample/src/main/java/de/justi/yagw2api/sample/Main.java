package de.justi.yagw2api.sample;

import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
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

			mainWindow = new MainWindow();

			mainWindow.setVisible(true);
			
			YAGW2APICore.setCurrentLocale(Locale.GERMANY);
			apiWrapper = YAGW2APICore.getWVWWrapper();
			final IWVWAnalyzer analyzer = YAGW2APIAnalyzer.getAnalyzer();

			// start the api wrapper
			apiWrapper.start();
			mainWindow.getMatchModel().wireUp(apiWrapper);
			
			// wire everything up
			apiWrapper.registerWVWMapListener(analyzer);
			apiWrapper.registerWVWMatchListener(analyzer);
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
