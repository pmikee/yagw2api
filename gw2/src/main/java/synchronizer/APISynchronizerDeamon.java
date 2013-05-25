package synchronizer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import synchronizer.poolaction.SynchronizeMatchesAction;

import api.service.IWVWService;
import api.service.dto.IWVWMatchDTO;

import com.google.common.util.concurrent.AbstractScheduledService;

public class APISynchronizerDeamon extends AbstractScheduledService {
	private static final Logger LOGGER = Logger.getLogger(APISynchronizerDeamon.class);
	
	private final IWVWService service;
	private final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			new Thread.UncaughtExceptionHandler() {
				public void uncaughtException(Thread t, Throwable e) {
					LOGGER.fatal("Uncought exception thrown in " + t.getName(), e);
				}
			}, false);
	
	public APISynchronizerDeamon(IWVWService service){
		checkNotNull(service);
		this.service = service;
	}
	
	@Override
	protected void runOneIteration() throws Exception {
		final long startTimestamp = System.currentTimeMillis();
		
		final List<String> matchIds = new ArrayList<String>();
		for (IWVWMatchDTO match : this.service.retrieveAllMatches().getMatches()){
			matchIds.add(match.getId());
		}
		this.pool.invoke(new SynchronizeMatchesAction(this.service, matchIds, 2));
		
		final long endTime = System.currentTimeMillis();
		final long executionTime = endTime-startTimestamp;
		LOGGER.debug("Done with "+this.getClass().getSimpleName()+" after "+executionTime+"ms");
	}

	@Override
	protected Scheduler scheduler() {
		return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.SECONDS);
	}
}
