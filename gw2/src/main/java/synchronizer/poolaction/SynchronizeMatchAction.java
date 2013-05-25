package synchronizer.poolaction;

import java.util.List;

import api.service.IWVWService;

public class SynchronizeMatchAction extends AbstractMatchIdAction<SynchronizeMatchAction> {
	private static final long serialVersionUID = 8391498327079686666L;
	public SynchronizeMatchAction(IWVWService service, List<String> matchIds, int chunkSize) {
		super(service, matchIds, chunkSize);
	}
	protected SynchronizeMatchAction(IWVWService service, List<String> matchIds, int chunkSize, int fromInclusive, int toExclusive){
		super(service, matchIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected SynchronizeMatchAction createSubTask(List<String> mapIds, int chunkSize, int fromInclusive, int toExclusive) {
		return new SynchronizeMatchAction(this.getService(), mapIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected void perform(String matchId) {
		System.out.println(this.getService().retrieveMatchDetails(matchId));
	}

}
