package de.justi.yagw2api.wrapper.wvw;

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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.event.WVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapListener;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchListener;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveEndOfBuffEvent;

public final class DefaultWVWWrapper implements WVWWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWWrapper.class);
	private WVWSynchronizer deamon = null;
	private final Map<WVWMatch, Collection<WVWMatchListener>> singleMatchListeners = new CopyOnWriteHashMap<>();
	private final Collection<WVWMatchListener> allMatchesListeners = new CopyOnWriteArrayList<>();
	private final Map<WVWMap, Collection<WVWMapListener>> singleMapListeners = new CopyOnWriteHashMap<>();
	private final Map<WVWMatch, Collection<WVWMapListener>> allMapsOfSingleMatchListeners = new CopyOnWriteHashMap<>();
	private final Collection<WVWMapListener> allMapsOfAllMatchesListeners = new CopyOnWriteArrayList<>();

	public DefaultWVWWrapper() {
	}

	private void initDemaonIfRequired() {
		if (this.deamon == null) {
			synchronized (this) {
				if (this.deamon == null) {
					this.deamon = new WVWSynchronizer();
					this.deamon.getChannel().register(this);
				}
			}
		}
	}

	@Override
	public void start() {
		this.initDemaonIfRequired();
		checkState(this.deamon != null);
		checkState(!this.deamon.isRunning());
		this.deamon.startAsync();
	}

	@Override
	public void stop() {
		checkState(this.deamon != null);
		checkState(this.deamon.isRunning());
		this.deamon.stopAsync();
	}

	@Override
	public boolean isRunning() {
		return (this.deamon != null) && this.deamon.isRunning();
	}

	@Subscribe
	public void onWVWMatchEvent(final WVWMatchEvent event) {
		checkNotNull(event);
		LOGGER.debug(this + " will now inform it's registered listeners about " + event);
		final WVWMatch match = event.getMatch();

		for (WVWMatchListener listener : this.allMatchesListeners) {
			this.notifyWVWMatchListener(listener, event);
		}
		if (this.singleMatchListeners.containsKey(match)) {
			for (WVWMatchListener listener : this.singleMatchListeners.get(match)) {
				this.notifyWVWMatchListener(listener, event);
			}
		}
	}

	private void notifyWVWMatchListener(final WVWMatchListener listener, final WVWMatchEvent event) {
		checkNotNull(listener);
		checkNotNull(event);
		LOGGER.trace("Going to notify " + listener + " about " + event);
		if (event instanceof WVWMatchScoresChangedEvent) {
			listener.onMatchScoreChangedEvent((WVWMatchScoresChangedEvent) event);
		} else if (event instanceof WVWInitializedMatchEvent) {
			listener.onInitializedMatchForWrapper((WVWInitializedMatchEvent) event);
		}
	}

	@Subscribe
	public void onWVWMapEvent(final WVWMapEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(this + " will now inform it's registered listeners about " + event);
		}
		final WVWMap map = event.getMap();
		final WVWMatch match = map.getMatch().get();

		final Collection<WVWMapListener> notifiedListeners = new ArrayList<WVWMapListener>();

		for (WVWMapListener listener : this.allMapsOfAllMatchesListeners) {
			this.notifyWVWMapListener(listener, event);
			notifiedListeners.add(listener);
		}
		if (this.allMapsOfSingleMatchListeners.containsKey(match)) {
			for (WVWMapListener listener : this.allMapsOfSingleMatchListeners.get(match)) {
				this.notifyWVWMapListener(listener, event);
				notifiedListeners.add(listener);
			}
		}
		if (this.singleMapListeners.containsKey(map)) {
			for (WVWMapListener listener : this.singleMapListeners.get(map)) {
				this.notifyWVWMapListener(listener, event);
				notifiedListeners.add(listener);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Notified " + notifiedListeners + " about " + event);
		}
	}

	private void notifyWVWMapListener(final WVWMapListener listener, final WVWMapEvent event) {
		checkNotNull(listener);
		checkNotNull(event);
		LOGGER.trace("Going to notify " + listener + " about " + event);
		if (event instanceof WVWMapScoresChangedEvent) {
			listener.onChangedMapScoreEvent((WVWMapScoresChangedEvent) event);

		} else if (event instanceof WVWObjectiveCaptureEvent) {
			listener.onObjectiveCapturedEvent((WVWObjectiveCaptureEvent) event);

		} else if (event instanceof WVWObjectiveEndOfBuffEvent) {
			listener.onObjectiveEndOfBuffEvent((WVWObjectiveEndOfBuffEvent) event);

		} else if (event instanceof WVWObjectiveClaimedEvent) {
			listener.onObjectiveClaimedEvent((WVWObjectiveClaimedEvent) event);
		}
	}

	@Override
	public void registerWVWMatchListener(final WVWMatch match, final WVWMatchListener listener) {
		checkNotNull(match);
		checkNotNull(listener);
		checkState(!this.singleMatchListeners.containsKey(match) || !this.singleMatchListeners.get(match).contains(listener));

		// add listener references
		if (!this.singleMatchListeners.containsKey(match)) {
			synchronized (this.singleMatchListeners) {
				if (!this.singleMatchListeners.containsKey(match)) {
					this.singleMatchListeners.put(match, new CopyOnWriteArrayList<WVWMatchListener>());
				}
			}
		}
		checkState(this.singleMatchListeners.containsKey(match));
		this.singleMatchListeners.get(match).add(listener);
	}

	@Override
	public void registerWVWMatchListener(final WVWMatchListener listener) {
		checkNotNull(listener);
		checkState(!this.allMatchesListeners.contains(listener));

		// add listener references
		this.allMatchesListeners.add(listener);
	}

	@Override
	public void unregisterWVWMatchListener(final WVWMatchListener listener) {
		checkNotNull(listener);

		// remove listener references
		for (WVWMatch key : this.singleMatchListeners.keySet()) {
			if (this.singleMatchListeners.get(key).contains(listener)) {
				this.singleMatchListeners.get(key).remove(listener);
			}
		}
		this.allMatchesListeners.remove(listener);
	}

	@Override
	public void unregisterWVWMapListener(final WVWMapListener listener) {
		checkNotNull(listener);

		// remove listener references
		for (WVWMap key : this.singleMapListeners.keySet()) {
			if (this.singleMapListeners.get(key).contains(listener)) {
				this.singleMapListeners.get(key).remove(listener);
			}
		}
		for (WVWMatch key : this.allMapsOfSingleMatchListeners.keySet()) {
			if (this.allMapsOfSingleMatchListeners.get(key).contains(listener)) {
				this.allMapsOfSingleMatchListeners.get(key).remove(listener);
			}
		}
		this.allMapsOfAllMatchesListeners.remove(listener);
	}

	@Override
	public void registerWVWMapListener(final WVWMapListener listener) {
		checkNotNull(listener);
		checkState(!this.allMapsOfAllMatchesListeners.contains(listener));

		// add listener references
		this.allMapsOfAllMatchesListeners.add(listener);
	}

	@Override
	public void registerWVWMapListener(final WVWMatch match, final WVWMapListener listener) {
		checkNotNull(match);
		checkNotNull(listener);
		checkState(!this.allMapsOfSingleMatchListeners.containsKey(match) || !this.allMapsOfSingleMatchListeners.get(match).contains(listener));

		// add listener references
		if (!this.allMapsOfSingleMatchListeners.containsKey(match)) {
			synchronized (this.allMapsOfSingleMatchListeners) {
				if (!this.allMapsOfSingleMatchListeners.containsKey(match)) {
					this.allMapsOfSingleMatchListeners.put(match, new CopyOnWriteArrayList<WVWMapListener>());
				}
			}
		}
		checkState(this.allMapsOfSingleMatchListeners.containsKey(match));
		this.allMapsOfSingleMatchListeners.get(match).add(listener);
	}

	@Override
	public void registerWVWMapListener(final WVWMap map, final WVWMapListener listener) {
		checkNotNull(map);
		checkNotNull(listener);
		checkState(!this.singleMapListeners.containsKey(map) || !this.singleMapListeners.get(map).contains(listener));

		// add listener references
		if (!this.singleMapListeners.containsKey(map)) {
			synchronized (this.singleMapListeners) {
				if (!this.singleMapListeners.containsKey(map)) {
					this.singleMapListeners.put(map, new CopyOnWriteArrayList<WVWMapListener>());
				}
			}
		}
		checkState(this.singleMapListeners.containsKey(map));
		this.singleMapListeners.get(map).add(listener);
	}

	@Override
	public Set<WVWMatch> getAllMatches() {
		return this.deamon.getAllMatches();
	}

	@Override
	public Set<World> getAllWorlds() {
		return this.deamon.getAllWorlds();
	}

	@Override
	public Map<String, WVWMatch> getAllMatchesMappedById() {
		final Map<String, WVWMatch> matchesMap = new HashMap<String, WVWMatch>();
		for (WVWMatch match : this.deamon.getAllMatches()) {
			matchesMap.put(match.getId(), match);
		}
		return ImmutableMap.copyOf(matchesMap);
	}

	@Override
	public Map<Integer, World> getAllWorldMappedById() {
		final Map<Integer, World> worldMap = new HashMap<Integer, World>();
		for (World world : this.deamon.getAllWorlds()) {
			worldMap.put(world.getId(), world);
		}
		return ImmutableMap.copyOf(worldMap);
	}
}
