package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

class WVWWrapper implements IWVWWrapper {
	private static final Logger LOGGER = Logger.getLogger(WVWWrapper.class);
	private final WVWSynchronizer deamon;
	private final Map<IWVWMatch, Collection<IWVWMatchListener>> singleMatchListeners = new CopyOnWriteHashMap<IWVWMatch, Collection<IWVWMatchListener>>();
	private final Collection<IWVWMatchListener> allMatchesListeners = new CopyOnWriteArrayList<IWVWMatchListener>();
	private final Map<IWVWMap, Collection<IWVWMapListener>> singleMapListeners = new CopyOnWriteHashMap<IWVWMap, Collection<IWVWMapListener>>();
	private final Map<IWVWMatch, Collection<IWVWMapListener>> allMapsOfSingleMatchListeners = new CopyOnWriteHashMap<IWVWMatch, Collection<IWVWMapListener>>();
	private final Collection<IWVWMapListener> allMapsOfAllMatchesListeners = new CopyOnWriteArrayList<IWVWMapListener>();

	public WVWWrapper() {
		this.deamon = new WVWSynchronizer();
		this.deamon.getChannel().register(this);
	}

	public void start() {
		checkState(!this.deamon.isRunning());
		this.deamon.startAndWait();
	}

	public void stop() {
		checkState(this.deamon.isRunning());
		this.deamon.stopAndWait();
	}

	@Override
	public boolean isRunning() {
		return this.deamon.isRunning();
	}

	@Subscribe
	public void onWVWMatchEvent(IWVWMatchEvent event) {
		checkNotNull(event);
		LOGGER.debug(this+" will now inform it's registered listeners about "+event);
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
	
	private void notifyWVWMatchListener(IWVWMatchListener listener, IWVWMatchEvent event) {
		checkNotNull(listener);
		checkNotNull(event);
		if(event instanceof IWVWMatchScoresChangedEvent) {
			listener.notifyAboutMatchScoreChangedEvent((IWVWMatchScoresChangedEvent)event);
		}
	}

	@Subscribe
	public void onWVWMapScoreChangedEvent(IWVWMapEvent event) {
		checkNotNull(event);
		checkArgument(event.getMap().getMatch().isPresent());
		LOGGER.debug(this+" will now inform it's registered listeners about "+event);
		final IWVWMap map = event.getMap();
		final IWVWMatch match = map.getMatch().get();

		for (IWVWMapListener listener : this.allMapsOfAllMatchesListeners) {
			this.notifyWVWMapListener(listener, event);
		}
		if (this.allMapsOfSingleMatchListeners.containsKey(match)) {
			for (IWVWMapListener listener : this.allMapsOfSingleMatchListeners.get(match)) {
				this.notifyWVWMapListener(listener, event);
			}
		}
		if (this.singleMapListeners.containsKey(map)) {
			for (IWVWMapListener listener : this.singleMapListeners.get(map)) {
				this.notifyWVWMapListener(listener, event);
			}
		}
	}
	
	private void notifyWVWMapListener(IWVWMapListener listener, IWVWMapEvent event) {
		checkNotNull(listener);
		checkNotNull(event);
		if(event instanceof IWVWMapScoresChangedEvent) {
			listener.notifyAboutChangedMapScoreEvent((IWVWMapScoresChangedEvent)event);
		}else if(event instanceof IWVWObjectiveCaptureEvent) {
			listener.notifyAboutObjectiveCapturedEvent((IWVWObjectiveCaptureEvent)event);
		}else if(event instanceof IWVWObjectiveEndOfBuffEvent) {
			listener.notifyAboutObjectiveEndOfBuffEvent((IWVWObjectiveEndOfBuffEvent)event);
		}
	}

	@Override
	public void registerWVWMatchListener(IWVWMatch match, IWVWMatchListener listener) {
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
	public void registerWVWMatchListener(IWVWMatchListener listener) {
		checkNotNull(listener);
		checkState(!this.allMatchesListeners.contains(listener));

		// add listener references
		this.allMatchesListeners.add(listener);
	}

	@Override
	public void unregisterWVWMatchListener(IWVWMatchListener listener) {
		checkNotNull(listener);

		// remove listener references
		if (this.singleMatchListeners.containsValue(listener)) {
			for (IWVWMatch key : this.singleMatchListeners.keySet()) {
				if (this.singleMatchListeners.get(key).contains(listener)) {
					this.singleMatchListeners.get(key).remove(listener);
				}
			}
		}
		this.allMatchesListeners.remove(listener);
	}

	@Override
	public void registerWVWMapListener(IWVWMapListener listener) {
		checkNotNull(listener);
		checkState(!this.allMapsOfAllMatchesListeners.contains(listener));

		// add listener references
		this.allMapsOfAllMatchesListeners.add(listener);
	}

	@Override
	public void registerWVWMapListener(IWVWMatch match, IWVWMapListener listener) {
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
	public void registerWVWMapListener(IWVWMap map, IWVWMapListener listener) {
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
}
