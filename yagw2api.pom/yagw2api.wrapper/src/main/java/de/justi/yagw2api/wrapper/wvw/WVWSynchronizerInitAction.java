package de.justi.yagw2api.wrapper.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.arenanet.dto.wvw.WVWMatchDTO;
import de.justi.yagw2api.wrapper.AbstractSynchronizerAction;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWDomainFactory;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.event.WVWEventFactory;
import de.justi.yagwapi.common.event.HasChannel;

final class WVWSynchronizerInitAction extends AbstractSynchronizerAction<WVWMatchDTO, WVWSynchronizerInitAction> implements HasChannel {
	private static final Logger LOGGER = LoggerFactory.getLogger(WVWSynchronizerInitAction.class);
	private static final long serialVersionUID = 2446713690087630720L;
	private static final int MAX_CHUNK_SIZE = 100; // init all matches with one
													// thread
	private static final WVWDomainFactory WVW_MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWDomainFactory();
	private static final WVWEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWDomainEventFactory();

	final Set<WVWMatch> matchReferencesBuffer;
	final Set<World> worldReferencesBuffer;
	final Map<String, WVWMatch> matchesBuffer;
	final EventBus eventBus;

	public WVWSynchronizerInitAction(final List<WVWMatchDTO> content) {
		this(ImmutableList.copyOf(content), MAX_CHUNK_SIZE, 0, content.size(), new HashSet<WVWMatch>(), new HashSet<World>(), new HashMap<String, WVWMatch>(), new EventBus(
				WVWSynchronizerInitAction.class.getName()));
	}

	private WVWSynchronizerInitAction(final List<WVWMatchDTO> content, final int chunkSize, final int fromInclusive, final int toExclusive,
			final Set<WVWMatch> matchReferencesBuffer, final Set<World> worldReferencesBuffer, final Map<String, WVWMatch> matchesBuffer, final EventBus eventBus) {
		super(content, chunkSize, fromInclusive, toExclusive);
		this.matchReferencesBuffer = checkNotNull(matchReferencesBuffer);
		this.worldReferencesBuffer = checkNotNull(worldReferencesBuffer);
		this.matchesBuffer = checkNotNull(matchesBuffer);
		this.eventBus = checkNotNull(eventBus);
	}

	@Override
	protected WVWSynchronizerInitAction createSubTask(final List<WVWMatchDTO> content, final int chunkSize, final int fromInclusive, final int toExclusive) {
		return new WVWSynchronizerInitAction(content, chunkSize, fromInclusive, toExclusive, this.matchReferencesBuffer, this.worldReferencesBuffer, this.matchesBuffer,
				this.eventBus);
	}

	@Override
	public EventBus getChannel() {
		return this.eventBus;
	}

	@Override
	protected void perform(final WVWMatchDTO content) {
		final long startTimestamp = System.currentTimeMillis();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to perform " + this.getClass().getSimpleName() + " using content=" + content);
		}
		final WVWMatch match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(content, YAGW2APIArenanet.INSTANCE.getCurrentLocale()).build();
		final long completedMatchModelBuildTimestamp = System.currentTimeMillis();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Done with build of " + WVWMatch.class.getSimpleName() + " for content=" + content + " after " + (completedMatchModelBuildTimestamp - startTimestamp)
					+ "ms");
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
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Initialized match " + match.getId() + " after " + (System.currentTimeMillis() - startTimestamp) + "ms");
		}
		this.eventBus.post(WVW_MODEL_EVENT_FACTORY.newInitializedMatchEvent(match));
	}

	public final Set<WVWMatch> getMatchReferencesBuffer() {
		return this.matchReferencesBuffer;
	}

	public final Set<World> getWorldReferencesBuffer() {
		return this.worldReferencesBuffer;
	}

	public final Map<String, WVWMatch> getMatchesBuffer() {
		return this.matchesBuffer;
	}

}
