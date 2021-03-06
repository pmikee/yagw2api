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

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.wrapper.map.MapWrapper;
import de.justi.yagw2api.wrapper.map.domain.MapDomainFactory;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;
import de.justi.yagw2api.wrapper.wvw.domain.WVWDomainFactory;
import de.justi.yagw2api.wrapper.wvw.event.WVWEventFactory;

public enum YAGW2APIWrapper {
	INSTANCE;
	private static final Logger LOGGER = LoggerFactory.getLogger(YAGW2APIWrapper.class);
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	private final ForkJoinPool forkJoinPool;
	private final WVWDomainFactory wvwDomainFactory;
	private final WVWEventFactory wvwDomainEventFactory;
	private final MapDomainFactory mapDomainFactory;
	private final WVWWrapper wvwWrapper;
	private final MapWrapper mapWrapper;

	private YAGW2APIWrapper() {
		final Injector injector = Guice.createInjector(new WrapperModule());
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new UncaughtExceptionHandler() {

					@Override
					public void uncaughtException(final Thread t, final Throwable e) {
						LOGGER.error("Uncought exception thrown in {}", t, e);
					}
				}, false);
		this.wvwDomainFactory = checkNotNull(injector.getInstance(WVWDomainFactory.class), "failed to inject WVWDomainFactory via %s", injector);
		this.mapDomainFactory = checkNotNull(injector.getInstance(MapDomainFactory.class), "failed to inject MapDomainFactory via %s", injector);
		this.wvwDomainEventFactory = checkNotNull(injector.getInstance(WVWEventFactory.class), "failed to inject WVWModelEventFactory via %s", injector);
		this.wvwWrapper = checkNotNull(injector.getInstance(WVWWrapper.class), "failed to inject WVWWrapper via %s", injector);
		this.mapWrapper = checkNotNull(injector.getInstance(MapWrapper.class), "failed to inject MapWrapper via %s", injector);
	}

	public ForkJoinPool getForkJoinPool() {
		return this.forkJoinPool;
	}

	public MapDomainFactory getMapDomainFactory() {
		return this.mapDomainFactory;
	}

	public WVWDomainFactory getWVWDomainFactory() {
		return this.wvwDomainFactory;
	}

	public WVWEventFactory getWVWDomainEventFactory() {
		return this.wvwDomainEventFactory;
	}

	public WVWWrapper getWVWWrapper() {
		return this.wvwWrapper;
	}

	public MapWrapper getMapWrapper() {
		return this.mapWrapper;
	}
}
