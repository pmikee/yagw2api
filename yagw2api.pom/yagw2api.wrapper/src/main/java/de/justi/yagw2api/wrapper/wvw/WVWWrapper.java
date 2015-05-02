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

import java.util.Map;
import java.util.Set;

import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapListener;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchListener;

public interface WVWWrapper {
	/**
	 * <p>
	 * start the arenanet api sync deamon
	 * </p>
	 * <p>
	 * initialization is performed asynchronously
	 * </p>
	 */
	void start();

	/**
	 * <p>
	 * stop the arenanet api sync deamon
	 * </p>
	 * <p>
	 * shutdown is performed asynchronously
	 * </p>
	 */
	void stop();

	/**
	 * check if the arenanet api sync deamon is running
	 * 
	 * @return
	 */
	boolean isRunning();

	/**
	 * retrieve unmodifiable access to all matches
	 * 
	 * @return
	 */
	Set<WVWMatch> getAllMatches();

	/**
	 * <p>
	 * retrieve unmodifiable access to all matches mapped by their id
	 * </p>
	 * <p>
	 * <strong>potentially expensive</strong>, because this map is build on each method call
	 * </p>
	 * 
	 * @return
	 */
	Map<String, WVWMatch> getAllMatchesMappedById();

	/**
	 * retrieve unmodifiable access to all worlds
	 * 
	 * @return
	 */
	Set<World> getAllWorlds();

	/**
	 * <p>
	 * retrieve unmodifiable access to all worlds mapped by their id
	 * </p>
	 * <p>
	 * <strong>potentially expensive</strong>, because this map is build on each method call
	 * </p>
	 * 
	 * @return
	 */
	Map<Integer, World> getAllWorldMappedById();

	/**
	 * register a listener for a single match
	 * 
	 * @param matchId
	 * @param listener
	 */
	void registerWVWMatchListener(WVWMatch match, WVWMatchListener listener);

	/**
	 * register a listener for all matches
	 * 
	 * @param listener
	 */
	void registerWVWMatchListener(WVWMatchListener listener);

	/**
	 * unregisters a given match listener
	 * 
	 * @param listener
	 */
	void unregisterWVWMatchListener(WVWMatchListener listener);

	/**
	 * register a listener for all maps in all matches
	 * 
	 * @param listener
	 */
	void registerWVWMapListener(WVWMapListener listener);

	/**
	 * register a listener for all maps of a given match
	 * 
	 * @param listener
	 */
	void registerWVWMapListener(WVWMatch match, WVWMapListener listener);

	/**
	 * register a listener for a single map
	 * 
	 * @param map
	 * @param listener
	 */
	void registerWVWMapListener(WVWMap map, WVWMapListener listener);

	/**
	 * unregister a given map listener
	 * 
	 * @param listener
	 */
	void unregisterWVWMapListener(WVWMapListener listener);
}
