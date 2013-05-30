package de.justi.yagw2api.core.wrapper;

import java.util.Set;

import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;


public interface IWVWWrapper{
	void start();	
	boolean isRunning();
	
	/**
	 * retrieve unmodifiable access to all matches
	 * @return
	 */
	Set<IWVWMatch> getAllMatches();
	
	/**
	 * retrieve unmodifiable access to all worlds
	 * @return
	 */
	Set<IWorld> getAllWorlds();
	
	/**
	 * register a listener for a single match
	 * @param matchId
	 * @param listener
	 */
	void registerWVWMatchListener(IWVWMatch match, IWVWMatchListener listener);
	/**
	 * register a listener for all matches
	 * @param listener
	 */
	void registerWVWMatchListener(IWVWMatchListener listener);
	
	/**
	 * unregisters a given match listener
	 * @param listener
	 */
	void unregisterWVWMatchListener(IWVWMatchListener listener);
	
	
	/**
	 * register a listener for all maps in all matches
	 * @param listener
	 */
	void registerWVWMapListener(IWVWMapListener listener);
	
	/**
	 * register a listener for all maps of a given match
	 * @param listener
	 */
	void registerWVWMapListener(IWVWMatch match, IWVWMapListener listener);
	
	/**
	 * register a listener for a single map
	 * @param map
	 * @param listener
	 */
	void registerWVWMapListener(IWVWMap map, IWVWMapListener listener);
}
