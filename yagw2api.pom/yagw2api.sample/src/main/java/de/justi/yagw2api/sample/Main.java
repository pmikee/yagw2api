package de.justi.yagw2api.sample;

import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.utils.PersistenceHelper;
import de.justi.yagw2api.analyzer.utils.YAGW2APIAnalyzerInjectionHelper;
import de.justi.yagw2api.core.YAGW2APIInjectionHelper;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.sample.view.MainWindow;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		
		try {
			checkState(PersistenceHelper.getSharedEntityManager().isOpen());
			final IWVWWrapper apiWrapper = YAGW2APIInjectionHelper.getInjector().getInstance(IWVWWrapper.class);
			final IWVWAnalyzer analyzer = YAGW2APIAnalyzerInjectionHelper.getInjector().getInstance(IWVWAnalyzer.class);
			
			// wire everything up
			apiWrapper.getChannel().register(new Object() {
				@Subscribe
				public void onIWVWMapScoreChangeEvent(IWVWMapScoresChangedEvent event) {
					analyzer.notifyAboutUpdatedMapScores(event.getMap());
				}
				@Subscribe
				public void onIWVWMatchScoreChangeEvent(IWVWMatchScoresChangedEvent event) {
					analyzer.notifyAboutUpdatedMatchScores(event.getMatch());
				}
				@Subscribe
				public void onIWVWObjectiveCaptureEvent(IWVWObjectiveCaptureEvent event) {
					analyzer.notifyAboutCapturedObjective(event.getSource());
				}
				@Subscribe
				public void onIWVWObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
					analyzer.notifyAboutEndOfBuffObjective(event.getSource());
				}				
			});
			
			// start the api wrapper
			apiWrapper.start();	

			final MainWindow mainWindow = new MainWindow();
			mainWindow.setVisible(true);
		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running " + Main.class.getName() + "#main(String[])", e);
		}
	}

}
