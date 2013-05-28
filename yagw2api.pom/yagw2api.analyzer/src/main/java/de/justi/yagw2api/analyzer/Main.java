package de.justi.yagw2api.analyzer;

import static com.google.common.base.Preconditions.checkState;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import de.justi.yagw2api.analyzer.utils.PersistenceHelper;
import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			checkState(PersistenceHelper.getSharedEntityManager().isOpen());
			final IWVWWrapper apiWrapper = InjectionHelper.getInjector().getInstance(IWVWWrapper.class);
			final IWVWAnalyzer analyzer = InjectionHelper.getInjector().getInstance(IWVWAnalyzer.class);
			
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
		} catch (Exception e) {
			LOGGER.fatal("Uncought exception while running " + Main.class.getName() + "#main(String[])", e);
		}
	}

}
