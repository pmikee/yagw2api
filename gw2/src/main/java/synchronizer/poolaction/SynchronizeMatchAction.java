package synchronizer.poolaction;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import utils.InjectionHelper;
import api.dto.IWVWMatchDTO;
import api.service.IWVWService;

public class SynchronizeMatchAction extends AbstractMatchIdAction<SynchronizeMatchAction> {
	private static final Logger LOGGER = Logger.getLogger(SynchronizeMatchAction.class);
	private static final IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);

	private static final long serialVersionUID = 8391498327079686666L;

	public SynchronizeMatchAction(List<String> matchIds, int chunkSize) {
		super(matchIds, chunkSize);
	}

	protected SynchronizeMatchAction(List<String> matchIds, int chunkSize, int fromInclusive, int toExclusive) {
		super(matchIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected SynchronizeMatchAction createSubTask(List<String> mapIds, int chunkSize, int fromInclusive, int toExclusive) {
		return new SynchronizeMatchAction(mapIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected void perform(String matchId) {
		LOGGER.trace("Going to synchronize matchId="+matchId);
		final IWVWMatchDTO matchDto = SERVICE.retrieveMatch(matchId).get();
		final IWVWMatch match = WVW_MODEL_FACTORY.createMatchBuilder().fromMatchDTO(matchDto, Locale.GERMAN).build();
		LOGGER.debug("Synchronized matchId="+matchId);
	}

}
