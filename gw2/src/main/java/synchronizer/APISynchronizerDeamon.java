package synchronizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import synchronizer.poolaction.SynchronizeMatchAction;
import utils.InjectionHelper;
import api.dto.IWVWMatchDTO;
import api.service.IWVWService;

import com.google.common.util.concurrent.AbstractScheduledService;

public class APISynchronizerDeamon extends AbstractScheduledService {
	private static final transient IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	private static final int CHUNK_SIZE = 2;
	private static final long INTERVAL_MILLIS = 3000; // 3s
	private static final Logger LOGGER = Logger.getLogger(APISynchronizerDeamon.class);
	
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
				}
			}, false);
	
	
	
	@Override
	protected void runOneIteration() throws Exception {
		final long startTimestamp = System.currentTimeMillis();
		
		final List<String> matchIds = new ArrayList<String>();
		for (IWVWMatchDTO match : SERVICE.retrieveAllMatches().getMatches()){
			matchIds.add(match.getId());
		}
		this.pool.invoke(new SynchronizeMatchAction(matchIds, CHUNK_SIZE));
		
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
