package de.justi.gw2.client.wvw;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;


import org.apache.log4j.Logger;


import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;

import de.justi.gw2.api.dto.IWVWMatchDTO;
import de.justi.gw2.api.dto.IWVWMatchesDTO;
import de.justi.gw2.api.service.IWVWService;
import de.justi.gw2.client.wvw.poolactions.SynchronizeMatchAction;
import de.justi.gw2.model.wvw.IWVWMatch;
import de.justi.gw2.model.wvw.IWVWModelFactory;
import de.justi.gw2.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.gw2.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.gw2.utils.InjectionHelper;

public class WVW extends AbstractScheduledService {
	private static final IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final long INTERVAL_MILLIS = 1000; // 3s
	private static final Logger LOGGER = Logger.getLogger(WVW.class);

	private final Map<String, IWVWMatch> matchesMappedById = new HashMap<String, IWVWMatch>(); 
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
				}
			}, false);

	public WVW() {
		// this.matchesMappedById.put("2-6",
		// WVW_MODEL_FACTORY.createMatchBuilder().fromMatchDTO(SERVICE.retrieveMatch("2-6").get(),
		// Locale.GERMAN).build());
		final IWVWMatchesDTO matchesDto = SERVICE.retrieveAllMatches();
		IWVWMatch match;
		for (IWVWMatchDTO matchDTO : matchesDto.getMatches()) {
			match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(matchDTO, Locale.GERMAN).build();
			match.getChannel().register(this);
			this.matchesMappedById.put(match.getId(), match);			
		}

	}
	
	@Subscribe
	public void onWVWObjectiveCaptureEvent(IWVWObjectiveCaptureEvent event) {
		System.out.println(event.toString()+" --> "+event.getNewOwningWorld().getName()+" captured "+event.getSource().getLabel()+" that was owned by "+(event.getPreviousOwningWorld().isPresent()?event.getPreviousOwningWorld().get().getName():"none"));
	}
	
	@Subscribe
	public void onWVWMatchScoreChangeEvent(IWVWMatchScoresChangedEvent event) {
		System.out.println(event);
	}

	@Override
	protected void runOneIteration() throws Exception {
		final long startTimestamp = System.currentTimeMillis();

		this.pool.invoke(new SynchronizeMatchAction(this.matchesMappedById));

		final long endTime = System.currentTimeMillis();
		final long executionTime = endTime - startTimestamp;
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Done with " + this.getClass().getSimpleName() + " iteration after " + executionTime + "ms");
		}
	}

	@Override
	protected Scheduler scheduler() {
		return AbstractScheduledService.Scheduler.newFixedDelaySchedule(INTERVAL_MILLIS, INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
	}
}
