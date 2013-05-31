package de.justi.yagw2api.sample;

import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.sample.view.MainWindow;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		
		try {
			checkState(YAGW2APIAnalyzerPersistence.getDefaultEM().isOpen());
			final IWVWWrapper apiWrapper = YAGW2APICore.getWVWWrapper();
			final IWVWAnalyzer analyzer = YAGW2APIAnalyzer.getAnalyzer();

			// start the api wrapper
			apiWrapper.start();	
			
			apiWrapper.getAllMatches();
			
			// wire everything up
			apiWrapper.registerWVWMapListener(analyzer);
			apiWrapper.registerWVWMatchListener(analyzer);
					

			final MainWindow mainWindow = new MainWindow();			
			mainWindow.getModel().wireUp(apiWrapper, apiWrapper.getAllMatches().iterator().next().getCenterMap());
			mainWindow.setVisible(true);
		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running " + Main.class.getName() + "#main(String[])", e);
		}
	}

}
