package de.justi.yagw2api.wrapper;

import java.util.concurrent.ForkJoinPool;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.wrapper.impl.Module;

public enum YAGW2APIWrapper {
	INSTANCE;
	private static final Logger LOGGER = Logger.getLogger(YAGW2APIWrapper.class);
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
					public void uncaughtException(Thread t, Throwable e) {
						LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
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
