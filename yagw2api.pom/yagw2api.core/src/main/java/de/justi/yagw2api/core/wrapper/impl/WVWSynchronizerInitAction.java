package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWModelFactory;


public class WVWSynchronizerInitAction extends AbstractSynchronizerAction<IWVWMatchDTO, WVWSynchronizerInitAction> {
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizerInitAction.class);
	private static final long serialVersionUID = 2446713690087630720L;
	private static final int MAX_CHUNK_SIZE = 1;
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APICore.getInjector().getInstance(IWVWModelFactory.class);
	
	final Set<IWVWMatch> matchReferencesBuffer;
	final Set<IWorld> worldReferencesBuffer;
	final Map<String, IWVWMatch> matchesBuffer;
	
	public WVWSynchronizerInitAction(List<IWVWMatchDTO> content) {
		this(ImmutableList.copyOf(content), MAX_CHUNK_SIZE, 0, content.size(), new HashSet<IWVWMatch>(), new HashSet<IWorld>(), new HashMap<String, IWVWMatch>());
	}
	private WVWSynchronizerInitAction(List<IWVWMatchDTO> content, int chunkSize, int fromInclusive, int toExclusive, Set<IWVWMatch> matchReferencesBuffer, Set<IWorld> worldReferencesBuffer, Map<String, IWVWMatch> matchesBuffer) {
		super(content, chunkSize, fromInclusive, toExclusive);
		this.matchReferencesBuffer = matchReferencesBuffer;
		this.worldReferencesBuffer = worldReferencesBuffer;
		this.matchesBuffer = matchesBuffer;
	}
	
	@Override
	protected WVWSynchronizerInitAction createSubTask(List<IWVWMatchDTO> content, int chunkSize, int fromInclusive, int toExclusive) {
		return new WVWSynchronizerInitAction(content, chunkSize, fromInclusive, toExclusive, this.matchReferencesBuffer, this.worldReferencesBuffer, this.matchesBuffer);
	}

	@Override
	protected void perform(IWVWMatchDTO content) {
		final long startTimestamp = System.currentTimeMillis();
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to perform "+this.getClass().getSimpleName()+" using content="+content);
		}
		final IWVWMatch match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(content, Locale.getDefault()).build();
		final long completedMatchModelBuildTimestamp = System.currentTimeMillis();
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace("Done with build of "+IWVWMatch.class.getSimpleName()+" for content="+content+" after "+(completedMatchModelBuildTimestamp-startTimestamp)+"ms");
		}
		match.getChannel().register(this);
		this.matchesBuffer.put(match.getId(), match);
		this.matchReferencesBuffer.add(match);
		checkState(!this.worldReferencesBuffer.contains(match.getBlueWorld()));
		checkState(!this.worldReferencesBuffer.contains(match.getRedWorld()));
		checkState(!this.worldReferencesBuffer.contains(match.getGreenWorld()));
		this.worldReferencesBuffer.add(match.getBlueWorld());
		this.worldReferencesBuffer.add(match.getRedWorld());
		this.worldReferencesBuffer.add(match.getGreenWorld());
	}
	
	public final Set<IWVWMatch> getMatchReferencesBuffer() {
		return matchReferencesBuffer;
	}
	public final Set<IWorld> getWorldReferencesBuffer() {
		return worldReferencesBuffer;
	}
	public final Map<String, IWVWMatch> getMatchesBuffer() {
		return matchesBuffer;
	}

}
