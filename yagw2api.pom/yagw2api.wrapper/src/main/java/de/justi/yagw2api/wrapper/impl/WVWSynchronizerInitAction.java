package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


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

import de.justi.yagw2api.arenanet.IWVWMatchDTO;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.IWVWModelFactory;
import de.justi.yagw2api.wrapper.IWorld;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagwapi.common.IHasChannel;

final class WVWSynchronizerInitAction extends AbstractSynchronizerAction<IWVWMatchDTO, WVWSynchronizerInitAction> implements IHasChannel {
	private static final Logger LOGGER = Logger.getLogger(WVWSynchronizerInitAction.class);
	private static final long serialVersionUID = 2446713690087630720L;
	private static final int MAX_CHUNK_SIZE = 100; // init all matches with one
													// thread
	private static final IWVWModelFactory WVW_MODEL_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelFactory();
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelEventFactory();

	final Set<IWVWMatch> matchReferencesBuffer;
	final Set<IWorld> worldReferencesBuffer;
	final Map<String, IWVWMatch> matchesBuffer;
	final EventBus eventBus;

	public WVWSynchronizerInitAction(List<IWVWMatchDTO> content) {
		this(ImmutableList.copyOf(content), MAX_CHUNK_SIZE, 0, content.size(), new HashSet<IWVWMatch>(), new HashSet<IWorld>(), new HashMap<String, IWVWMatch>(), new EventBus(
				WVWSynchronizerInitAction.class.getName()));
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
		final IWVWMatch match = WVW_MODEL_FACTORY.newMatchBuilder().fromMatchDTO(content, YAGW2APIArenanet.INSTANCE.getCurrentLocale()).build();
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
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Initialized match " + match.getId() + " after " + (System.currentTimeMillis() - startTimestamp) + "ms");
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
