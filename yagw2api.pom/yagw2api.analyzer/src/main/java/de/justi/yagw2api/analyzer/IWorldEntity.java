package de.justi.yagw2api.analyzer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import java.util.List;
import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.IWorldLocationType;

public interface IWorldEntity extends IEntity {
	Optional<String> getName(Locale locale);

	Optional<String> getName();

	Optional<String> getNameDE();

	Optional<String> getNameEN();

	Optional<String> getNameES();

	Optional<String> getNameFR();

	Optional<Integer> getOriginWorldId();

	IWorldLocationType getLocation();

	Optional<Locale> getWorldLocale();

	boolean addParticipatedAsRedInMatch(IWVWMatchEntity match);

	boolean addParticipatedAsBlueInMatch(IWVWMatchEntity match);

	boolean addParticipatedAsGreenInMatch(IWVWMatchEntity match);

	Iterable<IWVWMatchEntity> getParticipatedInMatches();

	List<IWVWMatchEntity> getParticipatedInMatchesAsRedWorld();

	List<IWVWMatchEntity> getParticipatedInMatchesAsGreenWorld();

	List<IWVWMatchEntity> getParticipatedInMatchesAsBlueWorld();

	/**
	 * <p>
	 * Synchronize this with the given {@link IWorld}
	 * </p>
	 * <p>
	 * Synchronization is not persisted directly, therefore you have to use
	 * {@link IWorldEnityDAO#save(IWorldEntity)} to persist them.
	 * </p>
	 * 
	 * @param model
	 * @return true if successfully, else false
	 */
	boolean synchronizeWithModel(IWorld model);
}
