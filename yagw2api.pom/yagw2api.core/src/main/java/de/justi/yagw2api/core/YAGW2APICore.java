package de.justi.yagw2api.core;

import static com.google.common.base.Preconditions.checkState;

import java.util.concurrent.ForkJoinPool;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.justi.yagw2api.core.arenanet.dto.impl.APIDTOModule;
import de.justi.yagw2api.core.arenanet.service.IWVWService;
import de.justi.yagw2api.core.arenanet.service.impl.APIServiceModule;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.impl.WrapperModule;
import de.justi.yagw2api.core.wrapper.model.impl.ModelModule;
import de.justi.yagw2api.core.wrapper.model.wvw.events.impl.WVWModelEventsModule;
import de.justi.yagw2api.core.wrapper.model.wvw.impl.WVWModelModule;

public enum YAGW2APICore {
	INSTANCE;
	private static final Logger LOGGER = Logger.getLogger(YAGW2APICore.class);

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
	public static IWVWService getLowLevelWVWService() {
		checkState(INSTANCE != null);
		checkState(INSTANCE.injector != null);
		return getInjector().getInstance(IWVWService.class);
	}

	/**
	 * <p>
	 * access to high level api wrapper that provides an event driven model
	 * access
	 * </p>
	 * 
	 * @return
	 */
	public static IWVWWrapper getWVWWrapper() {
		checkState(INSTANCE != null);
		checkState(INSTANCE.injector != null);
		return getInjector().getInstance(IWVWWrapper.class);
	}

	private final ForkJoinPool forkJoinPool;
	private final Injector injector;

	private YAGW2APICore() {
		this.injector = Guice.createInjector(new APIDTOModule(), new ModelModule(), new WVWModelModule(), new WVWModelEventsModule(), new APIServiceModule(), new WrapperModule());
		this.forkJoinPool = new ForkJoinPool(50, ForkJoinPool.defaultForkJoinWorkerThreadFactory,
				new Thread.UncaughtExceptionHandler() {
					public void uncaughtException(Thread t, Throwable e) {
						LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
					}
				}, false);
	}
}
