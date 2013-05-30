package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
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
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchesDTO;
import de.justi.yagw2api.core.arenanet.service.IWVWService;
import de.justi.yagw2api.core.wrapper.model.IEvent;
import de.justi.yagw2api.core.wrapper.model.IHasChannel;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWModelFactory;

class WVWSynchronizer extends AbstractScheduledService implements IHasChannel {
	private static final IWVWService SERVICE = YAGW2APICore.getLowLevelWVWService();
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APICore.getInjector().getInstance(IWVWModelFactory.class);
	private static final long DELAY_MILLIS = 50;
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizer.class);

	private final Map<String, IWVWMatch> matchesMappedById;
	
	private final Set<IWVWMatch> unmodifiableMatchReferences;
	private final Set<IWorld> unmodifiableWorldReferences;
	
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory,
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
		IWVWMatch match;
		
		final Set<IWVWMatch> unmodifiableMatchReferencesBuffer = new HashSet<IWVWMatch>();
		final Set<IWorld> unmodifiableWorldReferencesBuffer = new HashSet<IWorld>();
		
		final Map<String, IWVWMatch> matchesBuffer = new HashMap<String, IWVWMatch>();
		for (IWVWMatchDTO matchDTO : matchesDto.getMatches()) {
			match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(matchDTO, Locale.GERMAN).build();
			match.getChannel().register(this);
			matchesBuffer.put(match.getId(), match);
			unmodifiableMatchReferencesBuffer.add(match);
			checkState(!unmodifiableWorldReferencesBuffer.contains(match.getBlueWorld()));
			checkState(!unmodifiableWorldReferencesBuffer.contains(match.getRedWorld()));
			checkState(!unmodifiableWorldReferencesBuffer.contains(match.getGreenWorld()));
			unmodifiableWorldReferencesBuffer.add(match.getBlueWorld());
			unmodifiableWorldReferencesBuffer.add(match.getRedWorld());
			unmodifiableWorldReferencesBuffer.add(match.getGreenWorld());
		}
		this.matchesMappedById = ImmutableMap.copyOf(matchesBuffer);
		this.unmodifiableMatchReferences = ImmutableSet.copyOf(unmodifiableMatchReferencesBuffer);
		this.unmodifiableWorldReferences = ImmutableSet.copyOf(unmodifiableWorldReferencesBuffer);
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
