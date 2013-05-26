package client.wvw;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;

import org.apache.log4j.Logger;

import utils.InjectionHelper;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchesDTO;
import api.service.IWVWService;
import client.wvw.poolaction.SynchronizeMatchAction;

import com.google.common.util.concurrent.AbstractScheduledService;

public class WVW extends AbstractScheduledService {
	private static final transient IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final long INTERVAL_MILLIS = 3000; // 3s
	private static final Logger LOGGER = Logger.getLogger(WVW.class);

	private Map<String, IWVWMatch> matchesMappedById;
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
				}
			}, false);
	
	
	public WVW() {
		final IWVWMatchesDTO matchesDto = SERVICE.retrieveAllMatches();
		
		this.matchesMappedById  = new HashMap<String, IWVWMatch>();
		IWVWMatch match;
		for (IWVWMatchDTO matchDTO : matchesDto.getMatches()) {
			 match = WVW_MODEL_FACTORY.createMatchBuilder().fromMatchDTO(matchDTO, Locale.GERMAN).build();
			 this.matchesMappedById.put(match.getId(), match);
		}
		 
	}
	
	
	@Override
	protected void runOneIteration() throws Exception {
		final long startTimestamp = System.currentTimeMillis();
		
		this.pool.invoke(new SynchronizeMatchAction(this.matchesMappedById));
		
		final long endTime = System.currentTimeMillis();
		final long executionTime = endTime-startTimestamp;
		LOGGER.debug("Done with "+this.getClass().getSimpleName()+" after "+executionTime+"ms");
		if(executionTime > INTERVAL_MILLIS){
			LOGGER.error("Iteration of "+this.getClass().getSimpleName()+" took "+executionTime+"ms which is more than the interval of "+INTERVAL_MILLIS+"ms");
		}
	}

	@Override
	protected Scheduler scheduler() {
		return AbstractScheduledService.Scheduler.newFixedRateSchedule(INTERVAL_MILLIS, INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
	}
}
