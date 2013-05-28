package de.justi.yagw2api.wrapper.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;

import de.justi.yagw2api.api.dto.IWVWMatchDTO;
import de.justi.yagw2api.api.dto.IWVWMatchesDTO;
import de.justi.yagw2api.api.service.IWVWService;
import de.justi.yagw2api.utils.InjectionHelper;
import de.justi.yagw2api.wrapper.model.IEvent;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.model.wvw.IWVWModelFactory;

class WVWSynchronizer extends AbstractScheduledService {
	private static final IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final long DELAY_MILLIS = 50;
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizer.class);

	private final Map<String, IWVWMatch> matchesMappedById = new HashMap<String, IWVWMatch>();
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
				}
			}, false);

	public WVWSynchronizer() {
		final IWVWMatchesDTO matchesDto = SERVICE.retrieveAllMatches();
		IWVWMatch match;
		for (IWVWMatchDTO matchDTO : matchesDto.getMatches()) {
			match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(matchDTO, Locale.GERMAN).build();
			match.getChannel().register(this);
			this.matchesMappedById.put(match.getId(), match);
		}

	}

	@Subscribe
	public void onEvent(IEvent event) {
		System.out.println(event);
	}

	@Override
	protected void runOneIteration() throws Exception {
		final long startTimestamp = System.currentTimeMillis();

		this.pool.invoke(new WVWSynchronizerAction(this.matchesMappedById));

		final long endTime = System.currentTimeMillis();
		final long executionTime = endTime - startTimestamp;
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Done with " + this.getClass().getSimpleName() + " iteration after " + executionTime + "ms");
		}
	}

	@Override
	protected Scheduler scheduler() {
		return AbstractScheduledService.Scheduler.newFixedDelaySchedule(DELAY_MILLIS, DELAY_MILLIS, TimeUnit.MILLISECONDS);
	}
}
