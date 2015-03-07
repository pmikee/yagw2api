package de.justi.yagw2api.wrapper.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.arenanet.IWVWMatchesDTO;
import de.justi.yagw2api.arenanet.IWVWService;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagwapi.common.IEvent;
import de.justi.yagwapi.common.IHasChannel;

final class WVWSynchronizer extends AbstractScheduledService implements IHasChannel {
	private static final IWVWService SERVICE = YAGW2APIArenanet.getInstance().getWVWService();
	private static final long DELAY_MILLIS = 500;
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWSynchronizer.class);

	private Map<String, IWVWMatch> matchesMappedById = new CopyOnWriteHashMap<String, IWVWMatch>();

	private Set<IWVWMatch> matches = new CopyOnWriteArraySet<IWVWMatch>();
	private Set<IWorld> worlds = new CopyOnWriteArraySet<IWorld>();

	private final EventBus channel = new EventBus(this.getClass().getName());

	/**
	 * constructor
	 */
	public WVWSynchronizer() {

	}

	@Override
	public void startUp() {

		try {
			final long startTimestamp = System.currentTimeMillis();
			final IWVWMatchesDTO matchesDto = SERVICE.retrieveAllMatches();

			LOGGER.debug("Retrieved {} after {}ms", matchesDto, (System.currentTimeMillis() - startTimestamp));
			final WVWSynchronizerInitAction initAction = new WVWSynchronizerInitAction(Arrays.asList(matchesDto.getMatches()));
			initAction.getChannel().register(this);
			final Thread asyncStartup = new Thread() {
				@Override
				public void run() {
					try {
						YAGW2APIWrapper.INSTANCE.getForkJoinPool().invoke(initAction);
						final long endTimestamp = System.currentTimeMillis();
						LOGGER.info("Initialized {} in {}ms", WVWSynchronizer.this, (endTimestamp - startTimestamp));
					} catch (Exception e) {
						LOGGER.error("Exception thrown during initialization of {}", WVWSynchronizer.this);
					}
				}
			};
			asyncStartup.setDaemon(true);
			asyncStartup.start();
		} catch (Exception e) {
			LOGGER.error("Cought exception while initializing {}", this, e);
			throw e;
		}
	}

	public Set<IWVWMatch> getAllMatches() {
		return Collections.unmodifiableSet(this.matches);
	}

	public Set<IWorld> getAllWorlds() {
		return Collections.unmodifiableSet(this.worlds);
	}

	public Map<String, IWVWMatch> getMatchesMappedById() {
		return Collections.unmodifiableMap(this.matchesMappedById);
	}

	@Subscribe
	public void onEvent(final IEvent event) {
		checkNotNull(event);
		try {
			if (event instanceof IWVWInitializedMatchEvent) {
				final IWVWInitializedMatchEvent initializeEvent = (IWVWInitializedMatchEvent) event;
				initializeEvent.getMatch().getChannel().register(this);
				this.matchesMappedById.put(initializeEvent.getMatch().getId(), initializeEvent.getMatch());
				this.matches.add(initializeEvent.getMatch());
				this.worlds.add(initializeEvent.getMatch().getRedWorld());
				this.worlds.add(initializeEvent.getMatch().getBlueWorld());
				this.worlds.add(initializeEvent.getMatch().getGreenWorld());
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Added match with id=" + initializeEvent.getMatch().getId() + " to matches to synchronize.");
				}
			}
			this.channel.post(event);
		} catch (Exception e) {
			LOGGER.error("Cought exception while handling of {}", event, e);
			throw e;
		}
	}

	@Override
	protected void runOneIteration() throws Exception {
		try {
			final Map<String, IWVWMatch> defensiveCopyOfMatchesMappedById = Collections.unmodifiableMap(new HashMap<String, IWVWMatch>(this.getMatchesMappedById()));
			final long startTimestamp = System.currentTimeMillis();
			final WVWSynchronizerAction action = new WVWSynchronizerAction(defensiveCopyOfMatchesMappedById);
			YAGW2APIWrapper.INSTANCE.getForkJoinPool().invoke(action);

			final long endTime = System.currentTimeMillis();
			final long executionTime = endTime - startTimestamp;
			LOGGER.trace("Done with iteration of {} after {}ms", this, executionTime);
		} catch (Exception e) {
			LOGGER.error("Cought exception during run of iteration of {}", this, e);
			throw e;
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
