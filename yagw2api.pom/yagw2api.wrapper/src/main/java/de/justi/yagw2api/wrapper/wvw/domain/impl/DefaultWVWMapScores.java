package de.justi.yagw2api.wrapper.wvw.domain.impl;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.wvw.domain.WVWMap;
import de.justi.yagw2api.wrapper.wvw.event.WVWEventFactory;
import de.justi.yagw2api.wrapper.wvw.event.WVWMapScoresChangedEvent;

final class DefaultWVWMapScores extends AbstractWVWScores {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWMapScores.class);
	private static final WVWEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWDomainEventFactory();

	private final WVWMap map;

	public DefaultWVWMapScores(final WVWMap map) {
		this.map = checkNotNull(map);
	}

	@Override
	protected void onChange(final int deltaRed, final int deltaGreen, final int deltaBlue) {
		final WVWMapScoresChangedEvent event = WVW_MODEL_EVENT_FACTORY.newMapScoresChangedEvent(this.createUnmodifiableReference(), deltaRed, deltaGreen, deltaBlue,
				this.map.createUnmodifiableReference());
		checkState(event != null);
		LOGGER.trace("Going to post new {}", event);
		this.getChannel().post(event);
	}
}
