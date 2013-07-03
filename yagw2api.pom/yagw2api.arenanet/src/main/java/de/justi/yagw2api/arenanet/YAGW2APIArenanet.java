package de.justi.yagw2api.arenanet;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;
import java.util.concurrent.ForkJoinPool;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.arenanet.dto.impl.ArenanetDTOModule;
import de.justi.yagw2api.arenanet.service.IWVWService;
import de.justi.yagw2api.arenanet.service.impl.ArenanetServiceModule;

public enum YAGW2APIArenanet {
	INSTANCE;
	private static final Logger LOGGER = Logger.getLogger(YAGW2APIArenanet.class);
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
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	public static IWVWService getWVWService() {
		checkState(INSTANCE != null);
		checkState(INSTANCE.injector != null);
		return getInjector().getInstance(IWVWService.class);
	}

	public static Locale getCurrentLocale() {
		return INSTANCE.currentLocale;
	}

	public static void setCurrentLocale(Locale locale) {
		INSTANCE.currentLocale = checkNotNull(locale);
	}

	private final ForkJoinPool forkJoinPool;
	private final Injector injector;
	private Locale currentLocale = Locale.getDefault();

	private YAGW2APIArenanet() {
		this.injector = Guice.createInjector(new ArenanetDTOModule(), new ArenanetServiceModule());
		this.forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * THREAD_COUNT_PER_PROCESSOR, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
					}
				}, false);
	}
}
