package de.justi.yagw2api.explorer;

import java.io.IOException;
import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.explorer.view.MainWindow;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;

public final class Main {
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
	 * @throws MaryConfigurationException
	 * @throws SynthesisException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		YAGW2APIArenanet.INSTANCE.setCurrentLocale(Locale.getDefault());
		final IWVWWrapper apiWrapper = YAGW2APIWrapper.INSTANCE.getWVWWrapper();
		final IWVWAnalyzer analyzer = YAGW2APIAnalyzer.getAnalyzer();
		YAGW2APIAnalyzerPersistence.getDefaultEM(); // startup db connection
		final MainWindow mainWindow = new MainWindow();
		try {
			mainWindow.setVisible(true);

			// start the api wrapper
			apiWrapper.start();
			mainWindow.wireUp(apiWrapper);

			// wire everything up
			apiWrapper.registerWVWMapListener(analyzer);
			apiWrapper.registerWVWMatchListener(analyzer);
		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running " + Main.class.getName() + "#main(String[])", e);
		}
	}
}
