package de.justi.yagw2api.wrapper;

import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.impl.WrapperModule;
import de.justi.yagw2api.wrapper.model.impl.ModelModule;
import de.justi.yagw2api.wrapper.model.wvw.events.impl.WVWModelEventsModule;
import de.justi.yagw2api.wrapper.model.wvw.impl.WVWModelModule;

public enum YAGW2APIWrapper {
	INSTANCE;
	private static final Logger LOGGER = Logger.getLogger(YAGW2APIWrapper.class);
	private static final int THREAD_COUNT_PER_PROCESSOR = 2;

	public static ForkJoinPool getForkJoinPool() {
		checkState(INSTANCE != null);
		return INSTANCE.forkJoinPool;
	}

	public static Injector getInjector() {
		checkState(INSTANCE != null);
		checkState(INSTANCE.injector != null);
		return INSTANCE.injector;
	}

	/**
	 * <p>
	 * access to high level api wrapper that provides an event driven model access
	 * </p>
	 * 
	 * @return
	 */
	public static IWVWWrapper getWVWWrapper() {
		checkState(INSTANCE != null);
		checkState(INSTANCE.injector != null);
		return getInjector().getInstance(IWVWWrapper.class);
	}

	public static Locale getCurrentLocale() {
		return YAGW2APIArenanet.getInstance().getCurrentLocale();
	}

	public static void setCurrentLocale(Locale locale) {
		YAGW2APIArenanet.getInstance().setCurrentLocale(locale);
	}

	private final ForkJoinPool forkJoinPool;
	private final Injector injector;

	private YAGW2APIWrapper() {
		this.injector = Guice.createInjector(new ModelModule(), new WVWModelModule(), new WVWModelEventsModule(), new WrapperModule());
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
					}
				}, false);
	}
}
