package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.core.IHasChannel;
import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWModelFactory;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWModelEventFactory;

final class WVWSynchronizerInitAction extends AbstractSynchronizerAction<IWVWMatchDTO, WVWSynchronizerInitAction> implements IHasChannel {
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizerInitAction.class);
	private static final long serialVersionUID = 2446713690087630720L;
	private static final int MAX_CHUNK_SIZE = 100; // init all matches with one thread
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APICore.getInjector().getInstance(IWVWModelFactory.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APICore.getInjector().getInstance(IWVWModelEventFactory.class);

	final Set<IWVWMatch> matchReferencesBuffer;
	final Set<IWorld> worldReferencesBuffer;
	final Map<String, IWVWMatch> matchesBuffer;
	final EventBus eventBus;

	public WVWSynchronizerInitAction(List<IWVWMatchDTO> content) {
		this(ImmutableList.copyOf(content), MAX_CHUNK_SIZE, 0, content.size(), new HashSet<IWVWMatch>(), new HashSet<IWorld>(), new HashMap<String, IWVWMatch>(), new EventBus(WVWSynchronizerInitAction.class.getName()));
	}

	private WVWSynchronizerInitAction(List<IWVWMatchDTO> content, int chunkSize, int fromInclusive, int toExclusive, Set<IWVWMatch> matchReferencesBuffer, Set<IWorld> worldReferencesBuffer,
			Map<String, IWVWMatch> matchesBuffer, EventBus eventBus) {
		super(content, chunkSize, fromInclusive, toExclusive);
		this.matchReferencesBuffer = checkNotNull(matchReferencesBuffer);
		this.worldReferencesBuffer = checkNotNull(worldReferencesBuffer);
		this.matchesBuffer = checkNotNull(matchesBuffer);
		this.eventBus = checkNotNull(eventBus);
	}

	@Override
	protected WVWSynchronizerInitAction createSubTask(List<IWVWMatchDTO> content, int chunkSize, int fromInclusive, int toExclusive) {
		return new WVWSynchronizerInitAction(content, chunkSize, fromInclusive, toExclusive, this.matchReferencesBuffer, this.worldReferencesBuffer, this.matchesBuffer, this.eventBus);
	}

	@Override
	public EventBus getChannel() {
		return this.eventBus;
	}

	@Override
	protected void perform(IWVWMatchDTO content) {
		final long startTimestamp = System.currentTimeMillis();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to perform " + this.getClass().getSimpleName() + " using content=" + content);
		}
		final IWVWMatch match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(content, YAGW2APICore.getCurrentLocale()).build();
		final long completedMatchModelBuildTimestamp = System.currentTimeMillis();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Done with build of " + IWVWMatch.class.getSimpleName() + " for content=" + content + " after " + (completedMatchModelBuildTimestamp - startTimestamp) + "ms");
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
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("Initialized match "+match.getId()+ " after "+(System.currentTimeMillis()-startTimestamp)+"ms");
		}
		this.eventBus.post(WVW_MODEL_EVENT_FACTORY.newInitializedMatchEvent(match));
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
