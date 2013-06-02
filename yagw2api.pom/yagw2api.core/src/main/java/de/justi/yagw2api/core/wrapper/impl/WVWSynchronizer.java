package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchesDTO;
import de.justi.yagw2api.core.arenanet.service.IWVWService;
import de.justi.yagw2api.core.wrapper.model.IEvent;
import de.justi.yagw2api.core.wrapper.model.IHasChannel;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

class WVWSynchronizer extends AbstractScheduledService implements IHasChannel {
	private static final IWVWService SERVICE = YAGW2APICore.getLowLevelWVWService();
	private static final long DELAY_MILLIS = 50;
	private static final int POOL_THREADS_PER_PROCESSOR = 2;
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizer.class);

	private final Map<String, IWVWMatch> matchesMappedById;
	
	private final Set<IWVWMatch> unmodifiableMatchReferences;
	private final Set<IWorld> unmodifiableWorldReferences;
	
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors()*POOL_THREADS_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
				}
			}, false);
	private final EventBus channel = new EventBus(this.getClass().getName());

	/**
	 * constructor
	 */
	public WVWSynchronizer() {
		final IWVWMatchesDTO matchesDto = SERVICE.retrieveAllMatches();

		final WVWSynchronizerInitAction initAction = new WVWSynchronizerInitAction(Arrays.asList(matchesDto.getMatches()));
		this.pool.invoke(initAction);
		this.matchesMappedById = ImmutableMap.copyOf(initAction.getMatchesBuffer());
		this.unmodifiableMatchReferences = ImmutableSet.copyOf(initAction.getMatchReferencesBuffer());
		this.unmodifiableWorldReferences = ImmutableSet.copyOf(initAction.getWorldReferencesBuffer());
	}

	public Set<IWVWMatch> getAllMatches(){
		return this.unmodifiableMatchReferences;
	}
	
	public Set<IWorld> getAllWorlds(){
		return this.unmodifiableWorldReferences;
	}
	
	@Subscribe
	public void onEvent(IEvent event) {
		checkNotNull(event);
		this.channel.post(event);
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

	@Override
	public EventBus getChannel() {
		return this.channel;
	}
}
