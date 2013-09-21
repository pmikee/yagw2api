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


import java.util.Date;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWVWMatch;

public interface IWVWMatchEntityDAO {
	Optional<IWVWMatchEntity> newMatchEntityOf(IWVWMatch match);

	boolean save(IWVWMatchEntity entity);

	Optional<IWVWMatchEntity> findWVWMatchEntity(Date start, Date end, String originMatchId);

	/**
	 * <p>
	 * returns the {@link IWVWMatchEntity} that belongs to the given
	 * {@link IWVWMatch}
	 * </p>
	 * <p>
	 * if there is no such {@link IWVWMatchEntity} a new will be created
	 * </p>
	 * 
	 * @param world
	 * @return
	 */
	IWVWMatchEntity findOrCreateWVWMatchEntityOf(IWVWMatch match);

	/**
	 * <p>
	 * Synchronize this with the given {@link IWVWMatch}
	 * </p>
	 * <p>
	 * Synchronization is not persisted directly, therefore you have to use
	 * {@link IWVWMatchEntityDAO#save(IWVWMatchEntity)} to persist them.
	 * </p>
	 * 
	 * @param entity
	 * @param timestamp
	 * @param model
	 */
	void synchronizeEntityWithModel(IWVWMatchEntity entity, Date timestamp, IWVWMatch model);
}
