package de.justi.yagw2api.wrapper;

import java.util.Map;
import java.util.Set;

import de.justi.yagw2api.wrapper.model.IWorld;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.model.wvw.IWVWMatch;


public interface IWVWWrapper{
	/**
	 * <p>start the arenanet api sync deamon</p>
	 * <p>initialization is performed asynchronously</p>
	 */
	void start();	
	
	/**
	 * <p>start the arenanet api sync deamon</p>
	 * <p>initialization is performed synchronously</p>
	 */
	void startAndWait();
	/**
	 * <p>stop the arenanet api sync deamon</p>
	 * <p>shutdown is performed asynchronously</p>
	 */
	void stop();
	/**
	 * <p>stop the arenanet api sync deamon</p>
	 * <p>shutdown is performed synchronously</p>
	 */
	void stopAndWait();
	
	/**
	 * check if the arenanet api sync deamon is running
	 * @return
	 */
	boolean isRunning();
	
	/**
	 * retrieve unmodifiable access to all matches
	 * @return
	 */
	Set<IWVWMatch> getAllMatches();
	/**
	 * <p>retrieve unmodifiable access to all matches mapped by their id</p>
	 * <p><strong>potentially expensive</strong>, because this map is build on each method call</p>
	 * @return
	 */
	Map<String, IWVWMatch> getAllMatchesMappedById();
	
	/**
	 * retrieve unmodifiable access to all worlds
	 * @return
	 */
	Set<IWorld> getAllWorlds();
	/**
	 * <p>retrieve unmodifiable access to all worlds mapped by their id</p>
	 * <p><strong>potentially expensive</strong>, because this map is build on each method call</p>
	 * @return
	 */
	Map<Integer, IWorld> getAllWorldMappedById();
	
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
	
	/**
	 * unregister a given map listener
	 * @param listener
	 */
	void unregisterWVWMapListener(IWVWMapListener listener);
}
