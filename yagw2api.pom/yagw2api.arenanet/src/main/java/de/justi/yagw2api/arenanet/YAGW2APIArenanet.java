package de.justi.yagw2api.arenanet;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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

import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

import org.apache.log4j.Logger;

import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.arenanet.impl.Module;

public enum YAGW2APIArenanet {
	/**
	 * <p>
	 * Use {@link YAGW2APIArenanet#getInstance} instead.
	 * </p>
	 */
	INSTANCE;

	// CONSTS
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	// STATIC METHODS
	public static YAGW2APIArenanet getInstance() {
		return INSTANCE;
	}

	// FIELDS
	private final IWVWService wvwService;
	private final IGuildService guildService;
	private final IWorldService worldService;
	private final ForkJoinPool forkJoinPool;

	private Locale currentLocale = Locale.getDefault();

	// CONSTRUCTOR
	private YAGW2APIArenanet() {

		try {
			final Injector injector = Guice.createInjector(new Module());
			this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
					new Thread.UncaughtExceptionHandler() {
						@Override
						public void uncaughtException(Thread t, Throwable e) {
							Logger.getLogger(YAGW2APIArenanet.class).fatal("Uncought exception thrown in " + t.getName(), e);
						}
					}, false);
			this.guildService = injector.getInstance(IGuildService.class);
			this.wvwService = injector.getInstance(IWVWService.class);
			this.worldService = injector.getInstance(IWorldService.class);
			this.currentLocale = injector.getInstance(Locale.class);
		} catch (CreationException e) {
			Logger.getLogger(YAGW2APIArenanet.class).fatal("Failed to create " + Injector.class.getSimpleName() + " for " + Module.class.getSimpleName(), e);
			throw new IllegalStateException("Failed to create " + Injector.class.getSimpleName() + " for " + Module.class.getSimpleName(), e);
		}

	}

	// METHODS

	public static ForkJoinPool getForkJoinPool() {
		checkState(getInstance() != null);
		return getInstance().forkJoinPool;
	}

	/**
	 * <p>
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	public IWVWService getWVWService() {
		return this.wvwService;
	}

	/**
	 * <p>
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	public IWorldService getWorldService() {
		return this.worldService;
	}

	/**
	 * <p>
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	public IGuildService getGuildService() {
		return this.guildService;
	}

	public Locale getCurrentLocale() {
		return getInstance().currentLocale;
	}

	public void setCurrentLocale(Locale locale) {
		getInstance().currentLocale = checkNotNull(locale);
	}

}
