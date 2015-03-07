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

import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapEvent;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.IWorld;

final class WVWWrapper implements IWVWWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWWrapper.class);
	private WVWSynchronizer deamon = null;
	private final Map<IWVWMatch, Collection<IWVWMatchListener>> singleMatchListeners = new CopyOnWriteHashMap<IWVWMatch, Collection<IWVWMatchListener>>();
	private final Collection<IWVWMatchListener> allMatchesListeners = new CopyOnWriteArrayList<IWVWMatchListener>();
	private final Map<IWVWMap, Collection<IWVWMapListener>> singleMapListeners = new CopyOnWriteHashMap<IWVWMap, Collection<IWVWMapListener>>();
	private final Map<IWVWMatch, Collection<IWVWMapListener>> allMapsOfSingleMatchListeners = new CopyOnWriteHashMap<IWVWMatch, Collection<IWVWMapListener>>();
	private final Collection<IWVWMapListener> allMapsOfAllMatchesListeners = new CopyOnWriteArrayList<IWVWMapListener>();

	public WVWWrapper() {
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
	public void onWVWMatchEvent(final IWVWMatchEvent event) {
		checkNotNull(event);
		LOGGER.debug(this + " will now inform it's registered listeners about " + event);
		final IWVWMatch match = event.getMatch();

		for (IWVWMatchListener listener : this.allMatchesListeners) {
			this.notifyWVWMatchListener(listener, event);
		}
		if (this.singleMatchListeners.containsKey(match)) {
			for (IWVWMatchListener listener : this.singleMatchListeners.get(match)) {
				this.notifyWVWMatchListener(listener, event);
			}
		}
	}

	private void notifyWVWMatchListener(final IWVWMatchListener listener, final IWVWMatchEvent event) {
		checkNotNull(listener);
		checkNotNull(event);
		LOGGER.trace("Going to notify " + listener + " about " + event);
		if (event instanceof IWVWMatchScoresChangedEvent) {
			listener.onMatchScoreChangedEvent((IWVWMatchScoresChangedEvent) event);
		} else if (event instanceof IWVWInitializedMatchEvent) {
			listener.onInitializedMatchForWrapper((IWVWInitializedMatchEvent) event);
		}
	}

	@Subscribe
	public void onWVWMapEvent(final IWVWMapEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(this + " will now inform it's registered listeners about " + event);
		}
		final IWVWMap map = event.getMap();
		final IWVWMatch match = map.getMatch().get();

		final Collection<IWVWMapListener> notifiedListeners = new ArrayList<IWVWMapListener>();

		for (IWVWMapListener listener : this.allMapsOfAllMatchesListeners) {
			this.notifyWVWMapListener(listener, event);
			notifiedListeners.add(listener);
		}
		if (this.allMapsOfSingleMatchListeners.containsKey(match)) {
			for (IWVWMapListener listener : this.allMapsOfSingleMatchListeners.get(match)) {
				this.notifyWVWMapListener(listener, event);
				notifiedListeners.add(listener);
			}
		}
		if (this.singleMapListeners.containsKey(map)) {
			for (IWVWMapListener listener : this.singleMapListeners.get(map)) {
				this.notifyWVWMapListener(listener, event);
				notifiedListeners.add(listener);
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Notified " + notifiedListeners + " about " + event);
		}
	}

	private void notifyWVWMapListener(final IWVWMapListener listener, final IWVWMapEvent event) {
		checkNotNull(listener);
		checkNotNull(event);
		LOGGER.trace("Going to notify " + listener + " about " + event);
		if (event instanceof IWVWMapScoresChangedEvent) {
			listener.onChangedMapScoreEvent((IWVWMapScoresChangedEvent) event);

		} else if (event instanceof IWVWObjectiveCaptureEvent) {
			listener.onObjectiveCapturedEvent((IWVWObjectiveCaptureEvent) event);

		} else if (event instanceof IWVWObjectiveEndOfBuffEvent) {
			listener.onObjectiveEndOfBuffEvent((IWVWObjectiveEndOfBuffEvent) event);

		} else if (event instanceof IWVWObjectiveClaimedEvent) {
			listener.onObjectiveClaimedEvent((IWVWObjectiveClaimedEvent) event);
		}
	}

	@Override
	public void registerWVWMatchListener(final IWVWMatch match, final IWVWMatchListener listener) {
		checkNotNull(match);
		checkNotNull(listener);
		checkState(!this.singleMatchListeners.containsKey(match) || !this.singleMatchListeners.get(match).contains(listener));

		// add listener references
		if (!this.singleMatchListeners.containsKey(match)) {
			synchronized (this.singleMatchListeners) {
				if (!this.singleMatchListeners.containsKey(match)) {
					this.singleMatchListeners.put(match, new CopyOnWriteArrayList<IWVWMatchListener>());
				}
			}
		}
		checkState(this.singleMatchListeners.containsKey(match));
		this.singleMatchListeners.get(match).add(listener);
	}

	@Override
	public void registerWVWMatchListener(final IWVWMatchListener listener) {
		checkNotNull(listener);
		checkState(!this.allMatchesListeners.contains(listener));

		// add listener references
		this.allMatchesListeners.add(listener);
	}

	@Override
	public void unregisterWVWMatchListener(final IWVWMatchListener listener) {
		checkNotNull(listener);

		// remove listener references
		for (IWVWMatch key : this.singleMatchListeners.keySet()) {
			if (this.singleMatchListeners.get(key).contains(listener)) {
				this.singleMatchListeners.get(key).remove(listener);
			}
		}
		this.allMatchesListeners.remove(listener);
	}

	@Override
	public void unregisterWVWMapListener(final IWVWMapListener listener) {
		checkNotNull(listener);

		// remove listener references
		for (IWVWMap key : this.singleMapListeners.keySet()) {
			if (this.singleMapListeners.get(key).contains(listener)) {
				this.singleMapListeners.get(key).remove(listener);
			}
		}
		for (IWVWMatch key : this.allMapsOfSingleMatchListeners.keySet()) {
			if (this.allMapsOfSingleMatchListeners.get(key).contains(listener)) {
				this.allMapsOfSingleMatchListeners.get(key).remove(listener);
			}
		}
		this.allMapsOfAllMatchesListeners.remove(listener);
	}

	@Override
	public void registerWVWMapListener(final IWVWMapListener listener) {
		checkNotNull(listener);
		checkState(!this.allMapsOfAllMatchesListeners.contains(listener));

		// add listener references
		this.allMapsOfAllMatchesListeners.add(listener);
	}

	@Override
	public void registerWVWMapListener(final IWVWMatch match, final IWVWMapListener listener) {
		checkNotNull(match);
		checkNotNull(listener);
		checkState(!this.allMapsOfSingleMatchListeners.containsKey(match) || !this.allMapsOfSingleMatchListeners.get(match).contains(listener));

		// add listener references
		if (!this.allMapsOfSingleMatchListeners.containsKey(match)) {
			synchronized (this.allMapsOfSingleMatchListeners) {
				if (!this.allMapsOfSingleMatchListeners.containsKey(match)) {
					this.allMapsOfSingleMatchListeners.put(match, new CopyOnWriteArrayList<IWVWMapListener>());
				}
			}
		}
		checkState(this.allMapsOfSingleMatchListeners.containsKey(match));
		this.allMapsOfSingleMatchListeners.get(match).add(listener);
	}

	@Override
	public void registerWVWMapListener(final IWVWMap map, final IWVWMapListener listener) {
		checkNotNull(map);
		checkNotNull(listener);
		checkState(!this.singleMapListeners.containsKey(map) || !this.singleMapListeners.get(map).contains(listener));

		// add listener references
		if (!this.singleMapListeners.containsKey(map)) {
			synchronized (this.singleMapListeners) {
				if (!this.singleMapListeners.containsKey(map)) {
					this.singleMapListeners.put(map, new CopyOnWriteArrayList<IWVWMapListener>());
				}
			}
		}
		checkState(this.singleMapListeners.containsKey(map));
		this.singleMapListeners.get(map).add(listener);
	}

	@Override
	public Set<IWVWMatch> getAllMatches() {
		return this.deamon.getAllMatches();
	}

	@Override
	public Set<IWorld> getAllWorlds() {
		return this.deamon.getAllWorlds();
	}

	@Override
	public Map<String, IWVWMatch> getAllMatchesMappedById() {
		final Map<String, IWVWMatch> matchesMap = new HashMap<String, IWVWMatch>();
		for (IWVWMatch match : this.deamon.getAllMatches()) {
			matchesMap.put(match.getId(), match);
		}
		return ImmutableMap.copyOf(matchesMap);
	}

	@Override
	public Map<Integer, IWorld> getAllWorldMappedById() {
		final Map<Integer, IWorld> worldMap = new HashMap<Integer, IWorld>();
		for (IWorld world : this.deamon.getAllWorlds()) {
			worldMap.put(world.getId(), world);
		}
		return ImmutableMap.copyOf(worldMap);
	}
}
