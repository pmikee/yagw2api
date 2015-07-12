package de.justi.yagw2api.arenanet.v1;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public enum YAGW2APIArenanetV1 implements Arenanet, UncaughtExceptionHandler {
	/**
	 * <p>
	 * Use {@link YAGW2APIArenanet#getInstance} instead.
	 * </p>
	 */
	INSTANCE;

	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(YAGW2APIArenanetV1.class);
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	// STATIC METHODS
	public static YAGW2APIArenanetV1 getInstance() {
		return INSTANCE;
	}

	// FIELDS
	@Inject
	private WVWService wvwService = null;
	@Inject
	private GuildService guildService = null;
	@Inject
	private WorldService worldService = null;
	@Inject
	private MapService mapService = null;
	@Inject
	private MapTileService mapTileService = null;
	@Inject
	private MapFloorService mapFloorService = null;
	@Inject
	private MapContinentService mapContinentService = null;
	@Inject
	private Locale currentLocale = null;

	private final ForkJoinPool forkJoinPool;

	// CONSTRUCTOR
	private YAGW2APIArenanetV1() {
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory, this, false);
		final Injector injector = Guice.createInjector(new ArenanetModule());
		injector.injectMembers(this);
		checkNotNull(this.wvwService, "missing wvwService");
		checkNotNull(this.guildService, "missing guildService");
		checkNotNull(this.worldService, "missing worldService");
		checkNotNull(this.mapService, "missing mapService");
		checkNotNull(this.mapTileService, "missing mapTileService");
		checkNotNull(this.mapFloorService, "missing mapFloorService");
		checkNotNull(this.mapContinentService, "missing mapContinentService");
		checkNotNull(this.currentLocale, "missing currentLocale");
	}

	// METHODS
	@Override
	public void uncaughtException(final Thread t, final Throwable e) {
		LOGGER.error("Uncought exception thrown in {}", t, e);
	}

	public static ForkJoinPool getForkJoinPool() {
		checkState(getInstance() != null);
		return getInstance().forkJoinPool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.justi.yagw2api.arenanet.Arenanet#getWVWService()
	 */
	@Override
	public WVWService getWVWService() {
		return this.wvwService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.justi.yagw2api.arenanet.Arenanet#getWorldService()
	 */
	@Override
	public WorldService getWorldService() {
		return this.worldService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.justi.yagw2api.arenanet.Arenanet#getGuildService()
	 */
	@Override
	public GuildService getGuildService() {
		return this.guildService;
	}

	@Override
	public MapTileService getMapTileService() {
		return this.mapTileService;
	}

	@Override
	public MapFloorService getMapFloorService() {
		return this.mapFloorService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.justi.yagw2api.arenanet.Arenanet#getCurrentLocale()
	 */
	@Override
	public Locale getCurrentLocale() {
		return this.currentLocale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.justi.yagw2api.arenanet.Arenanet#setCurrentLocale(java.util.Locale)
	 */
	@Override
	public void setCurrentLocale(final Locale locale) {
		this.currentLocale = checkNotNull(locale);
	}

	@Override
	public MapService getMapService() {
		return this.mapService;
	}

	@Override
	public MapContinentService getMapContinentService() {
		return this.mapContinentService;
	}

}
