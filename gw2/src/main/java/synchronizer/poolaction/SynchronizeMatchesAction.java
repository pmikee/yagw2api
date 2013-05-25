package synchronizer.poolaction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;

import api.service.IWVWService;

public class SynchronizeMatchesAction extends RecursiveAction{
	private static final long serialVersionUID = -2142136115104413103L;
	private static final Logger LOGGER = Logger.getLogger(SynchronizeMatchesAction.class);
	private final int chunkSize;
	private final List<String> matchIds;
	private final int fromInclusive;
	private final int toExclusive;
	private final IWVWService service;
	
	public SynchronizeMatchesAction(IWVWService service, List<String> matchIds, int chunkSize){
		this(checkNotNull(service), ImmutableList.copyOf(checkNotNull(matchIds)), chunkSize,0, matchIds.size());
	}
	private SynchronizeMatchesAction(IWVWService service, List<String> matchIds, int chunkSize, int fromInclusive, int toExclusive){
		checkArgument(chunkSize > 0);
		checkNotNull(matchIds);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		checkNotNull(service);
		this.service = service;
		this.chunkSize = chunkSize;
		this.matchIds = matchIds;
		this.fromInclusive = fromInclusive;
		this.toExclusive = toExclusive;
		LOGGER.trace("New " + this.getClass().getSimpleName() + " that handles match ids " + this.matchIds.subList(this.fromInclusive, this.toExclusive));
	}
	
	protected SynchronizeMatchesAction createSubTask(List<String> mapIds, int chunkSize, int fromInclusive, int toExclusive) {
		return new SynchronizeMatchesAction(this.service, mapIds, chunkSize, fromInclusive, toExclusive);
	}
	
	private void synchronizeMatch(String matchId){
		this.service.retrieveMatchDetails(matchId);
	}
	
	@Override
	protected void compute() {
		try {
			if (this.toExclusive - this.fromInclusive <= this.chunkSize) {
				// compute directly
				for (int index = this.fromInclusive; index < this.toExclusive; index++) {
					this.synchronizeMatch(this.matchIds.get(index));
				}
				return;
			} else {
				// fork
				final int splitAtIndex = this.fromInclusive + ((this.toExclusive - this.fromInclusive) / 2);
				invokeAll(this.createSubTask(this.matchIds, this.chunkSize, this.fromInclusive, splitAtIndex), this.createSubTask(this.matchIds, this.chunkSize, splitAtIndex,
						this.toExclusive));
			}
		} catch (Exception e) {
			LOGGER.fatal("Failed during execution of "+this.getClass().getSimpleName()+" computing "+this.fromInclusive+"-"+this.toExclusive,e);
		}
	}

}
