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

import java.util.concurrent.ForkJoinPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.wrapper.domain.DomainFactory;
import de.justi.yagw2api.wrapper.domain.map.MapDomainFactory;
import de.justi.yagw2api.wrapper.domain.wvw.WVWDomainFactory;
import de.justi.yagw2api.wrapper.domain.wvw.event.WVWModelEventFactory;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;

public enum YAGW2APIWrapper {
	INSTANCE;
	private static final Logger LOGGER = LoggerFactory.getLogger(YAGW2APIWrapper.class);
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	private final ForkJoinPool forkJoinPool;
	private final DomainFactory domainFactory;
	private final WVWDomainFactory wvwDomainFactory;
	private final WVWModelEventFactory wvwDomainEventFactory;
	private final MapDomainFactory mapDomainFactory;
	private final WVWWrapper wrapper;

	private YAGW2APIWrapper() {
		final Injector injector = Guice.createInjector(new Module());
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(final Thread t, final Throwable e) {
						LOGGER.error("Uncought exception thrown in {}", t, e);
					}
				}, false);
		this.domainFactory = injector.getInstance(DomainFactory.class);
		this.wvwDomainFactory = injector.getInstance(WVWDomainFactory.class);
		this.mapDomainFactory = injector.getInstance(MapDomainFactory.class);
		this.wvwDomainEventFactory = injector.getInstance(WVWModelEventFactory.class);
		this.wrapper = injector.getInstance(WVWWrapper.class);
	}

	public ForkJoinPool getForkJoinPool() {
		return this.forkJoinPool;
	}

	public DomainFactory getDomainFactory() {
		return this.domainFactory;
	}

	public MapDomainFactory getMapDomainFactory() {
		return this.mapDomainFactory;
	}

	public WVWDomainFactory getWVWDomainFactory() {
		return this.wvwDomainFactory;
	}

	public WVWModelEventFactory getWVWDomainEventFactory() {
		return this.wvwDomainEventFactory;
	}

	public WVWWrapper getWVWWrapper() {
		return this.wrapper;
	}
}
