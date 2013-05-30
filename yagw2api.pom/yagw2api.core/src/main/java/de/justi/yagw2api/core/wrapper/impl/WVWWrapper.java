package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.eventbus.Subscribe;
import com.sun.jersey.client.impl.CopyOnWriteHashMap;

import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.AbstractHasChannel;
import de.justi.yagw2api.core.wrapper.model.IEvent;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;

class WVWWrapper extends AbstractHasChannel implements IWVWWrapper {
	private final WVWSynchronizer deamon;
	private final Map<IWVWMatch, IWVWMatchListener> singleMatchListeners = new CopyOnWriteHashMap<IWVWMatch, IWVWMatchListener>();
	private final Collection<IWVWMatchListener> allMatchesListeners = new CopyOnWriteArrayList<IWVWMatchListener>();
	private final Map<IWVWMap, IWVWMapListener> singleMapListeners = new CopyOnWriteHashMap<IWVWMap, IWVWMapListener>();
	private final Map<IWVWMatch, IWVWMapListener> allMapsOfSingleMatchListeners = new CopyOnWriteHashMap<IWVWMatch, IWVWMapListener>();
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
	
	@Subscribe
	public void onEvent(IEvent event) {
		this.getChannel().post(event);
	}

	@Override
	public boolean isRunning() {
		return this.deamon.isRunning();
	}

	@Override
	public void registerWVWMatchListener(IWVWMatch match, IWVWMatchListener listener) {
		checkNotNull(match);
		checkNotNull(listener);
		checkState(this.deamon.isRunning());
		checkState(!this.singleMatchListeners.containsKey(match));

		// add listener references
		this.singleMatchListeners.put(match, listener);
	}

	@Override
	public void registerWVWMatchListener(IWVWMatchListener listener) {
		checkNotNull(listener);
		checkState(this.deamon.isRunning());
		checkState(!this.allMatchesListeners.contains(listener));
		
		// add listener references
		this.allMatchesListeners.add(listener);
	}

	@Override
	public void unregisterWVWMatchListener(IWVWMatchListener listener) {
		checkNotNull(listener);
		checkState(this.deamon.isRunning());	
		
		// remove listener references
		if(this.singleMatchListeners.containsValue(listener)) {
			for(IWVWMatch key : this.singleMatchListeners.keySet()) {
				if(this.singleMatchListeners.get(key).equals(listener)) {
					this.singleMatchListeners.remove(key);
				}
			}
		}
		this.allMatchesListeners.remove(listener);
	}

	@Override
	public void registerWVWMapListener(IWVWMapListener listener) {
		checkNotNull(listener);
		checkState(this.deamon.isRunning());
		checkState(!this.allMapsOfAllMatchesListeners.contains(listener));
		
		
		// add listener references
		this.allMapsOfAllMatchesListeners.add(listener);
	}

	@Override
	public void registerWVWMapListener(IWVWMatch match, IWVWMapListener listener) {
		checkNotNull(match);
		checkNotNull(listener);
		checkState(this.deamon.isRunning());
		checkState(!this.allMapsOfSingleMatchListeners.containsKey(match));

		// add listener references
		this.allMapsOfSingleMatchListeners.put(match, listener);
	}

	@Override
	public void registerWVWMapListener(IWVWMap map, IWVWMapListener listener) {
		checkNotNull(map);
		checkNotNull(listener);
		checkState(this.deamon.isRunning());
		checkState(!this.singleMapListeners.containsKey(map));

		// add listener references
		this.singleMapListeners.put(map, listener);
	}

	@Override
	public Collection<IWVWMatch> getAllMatches() {
		checkState(this.deamon.isRunning());
		return null;
	}

	@Override
	public Collection<IWorld> getAllWorlds() {
		checkState(this.deamon.isRunning());
		return null;
	}
}
