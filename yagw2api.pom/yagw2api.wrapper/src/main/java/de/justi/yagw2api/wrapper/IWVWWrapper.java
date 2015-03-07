package de.justi.yagw2api.wrapper;

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

public interface IWVWWrapper {
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
	Set<IWVWMatch> getAllMatches();

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
	Map<String, IWVWMatch> getAllMatchesMappedById();

	/**
	 * retrieve unmodifiable access to all worlds
	 * 
	 * @return
	 */
	Set<IWorld> getAllWorlds();

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
	Map<Integer, IWorld> getAllWorldMappedById();

	/**
	 * register a listener for a single match
	 * 
	 * @param matchId
	 * @param listener
	 */
	void registerWVWMatchListener(IWVWMatch match, IWVWMatchListener listener);

	/**
	 * register a listener for all matches
	 * 
	 * @param listener
	 */
	void registerWVWMatchListener(IWVWMatchListener listener);

	/**
	 * unregisters a given match listener
	 * 
	 * @param listener
	 */
	void unregisterWVWMatchListener(IWVWMatchListener listener);

	/**
	 * register a listener for all maps in all matches
	 * 
	 * @param listener
	 */
	void registerWVWMapListener(IWVWMapListener listener);

	/**
	 * register a listener for all maps of a given match
	 * 
	 * @param listener
	 */
	void registerWVWMapListener(IWVWMatch match, IWVWMapListener listener);

	/**
	 * register a listener for a single map
	 * 
	 * @param map
	 * @param listener
	 */
	void registerWVWMapListener(IWVWMap map, IWVWMapListener listener);

	/**
	 * unregister a given map listener
	 * 
	 * @param listener
	 */
	void unregisterWVWMapListener(IWVWMapListener listener);
}
