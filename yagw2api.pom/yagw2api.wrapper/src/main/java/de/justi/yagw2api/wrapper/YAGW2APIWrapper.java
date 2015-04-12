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

import de.justi.yagw2api.wrapper.domain.IModelFactory;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWModelFactory;
import de.justi.yagw2api.wrapper.domain.wvw.event.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.wvw.IWVWWrapper;

public enum YAGW2APIWrapper {
	INSTANCE;
	private static final Logger LOGGER = LoggerFactory.getLogger(YAGW2APIWrapper.class);
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	private final ForkJoinPool forkJoinPool;
	private final IModelFactory modelFactory;
	private final IWVWModelFactory wvwModelFactory;
	private final IWVWModelEventFactory wvwModelEventFactory;
	private final IWVWWrapper wrapper;

	private YAGW2APIWrapper() {
		final Injector injector = Guice.createInjector(new Module());
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(final Thread t, final Throwable e) {
						LOGGER.error("Uncought exception thrown in {}", t, e);
					}
				}, false);
		this.modelFactory = injector.getInstance(IModelFactory.class);
		this.wvwModelFactory = injector.getInstance(IWVWModelFactory.class);
		this.wvwModelEventFactory = injector.getInstance(IWVWModelEventFactory.class);
		this.wrapper = injector.getInstance(IWVWWrapper.class);
	}

	public ForkJoinPool getForkJoinPool() {
		return this.forkJoinPool;
	}

	public IModelFactory getModelFactory() {
		return this.modelFactory;
	}

	public IWVWModelFactory getWVWModelFactory() {
		return this.wvwModelFactory;
	}

	public IWVWModelEventFactory getWVWModelEventFactory() {
		return this.wvwModelEventFactory;
	}

	public IWVWWrapper getWVWWrapper() {
		return this.wrapper;
	}
}
