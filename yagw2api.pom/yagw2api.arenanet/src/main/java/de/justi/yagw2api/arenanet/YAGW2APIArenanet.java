package de.justi.yagw2api.arenanet;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.arenanet.impl.Module;

public enum YAGW2APIArenanet implements IArenanet {
	/**
	 * <p>
	 * Use {@link YAGW2APIArenanet#getInstance} instead.
	 * </p>
	 */
	INSTANCE;

	// CONSTS
	private static final Logger LOGGER = LoggerFactory.getLogger(YAGW2APIArenanet.class);
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	// STATIC METHODS
	public static YAGW2APIArenanet getInstance() {
		return INSTANCE;
	}

	// FIELDS
	private final IWVWService wvwService;
	private final IGuildService guildService;
	private final IWorldService worldService;
	private final IMapTileService mapTileService;
	private final ForkJoinPool forkJoinPool;

	private Locale currentLocale = Locale.getDefault();

	// CONSTRUCTOR
	private YAGW2APIArenanet() {
		final Injector injector = Guice.createInjector(new Module());
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(final Thread t, final Throwable e) {
						LOGGER.error("Uncought exception thrown in {}", t, e);
					}
				}, false);
		this.guildService = injector.getInstance(IGuildService.class);
		this.wvwService = injector.getInstance(IWVWService.class);
		this.worldService = injector.getInstance(IWorldService.class);
		this.mapTileService = injector.getInstance(IMapTileService.class);
		this.currentLocale = injector.getInstance(Locale.class);
	}

	// METHODS

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
	public IWVWService getWVWService() {
		return this.wvwService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.justi.yagw2api.arenanet.Arenanet#getWorldService()
	 */
	@Override
	public IWorldService getWorldService() {
		return this.worldService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.justi.yagw2api.arenanet.Arenanet#getGuildService()
	 */
	@Override
	public IGuildService getGuildService() {
		return this.guildService;
	}

	@Override
	public IMapTileService getMapTileService() {
		return this.mapTileService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.justi.yagw2api.arenanet.Arenanet#getCurrentLocale()
	 */
	@Override
	public Locale getCurrentLocale() {
		return getInstance().currentLocale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.justi.yagw2api.arenanet.Arenanet#setCurrentLocale(java.util.Locale)
	 */
	@Override
	public void setCurrentLocale(final Locale locale) {
		getInstance().currentLocale = checkNotNull(locale);
	}

}
