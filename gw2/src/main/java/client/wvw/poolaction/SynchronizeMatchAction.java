package client.wvw.poolaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.wvw.IWVWMatch;

import org.apache.log4j.Logger;

import utils.InjectionHelper;
import api.dto.IWVWMatchDTO;
import api.service.IWVWService;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SynchronizeMatchAction extends AbstractMatchIdAction<SynchronizeMatchAction> {
	private static final long serialVersionUID = 8391498327079686666L;
	private static final Logger LOGGER = Logger.getLogger(SynchronizeMatchAction.class);
	private static final IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);

	private final Map<String, IWVWMatch> matchesMappedById;


	public SynchronizeMatchAction(Map<String, IWVWMatch> matchesMappedById) {
		this(ImmutableMap.copyOf(matchesMappedById), matchesMappedById.keySet(), 2, 0, matchesMappedById.size());
	}

	private SynchronizeMatchAction(Map<String, IWVWMatch> matchesMappedById, Collection<String> matchIds, int chunkSize, int fromInclusive, int toExclusive) {
		super(ImmutableList.copyOf(matchIds), chunkSize, fromInclusive, toExclusive);
		this.matchesMappedById = matchesMappedById;
	}

	@Override
	protected SynchronizeMatchAction createSubTask(List<String> mapIds, int chunkSize, int fromInclusive, int toExclusive) {
		return new SynchronizeMatchAction(this.matchesMappedById, mapIds, chunkSize, fromInclusive, toExclusive);
	}

	@Override
	protected void perform(String matchId) {
		LOGGER.trace("Going to synchronize matchId="+matchId);
		final Optional<IWVWMatchDTO> matchDTO = SERVICE.retrieveMatch(matchId);
		if(matchDTO.isPresent() && matchDTO.get().getDetails().isPresent()) {
			final IWVWMatch matchModel = this.matchesMappedById.get(matchId);
			if(!matchModel.searchWorldsByNamePattern(Pattern.compile("Drakkar.*")).isEmpty()) {
				System.out.println(matchDTO.get().getDetails().get());
			}
		}else {
			LOGGER.error("Failed to retrieve "+IWVWMatchDTO.class.getSimpleName()+" for matchId="+matchId);
		}
		
		LOGGER.debug("Synchronized matchId="+matchId);
	}

}
