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
import de.justi.yagw2api.wrapper.wvw.domain.WVWMatch;
import de.justi.yagw2api.wrapper.wvw.event.WVWEventFactory;
import de.justi.yagw2api.wrapper.wvw.event.WVWMatchScoresChangedEvent;

final class DefaultWVWMatchScores extends AbstractWVWScores {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWMatchScores.class);
	private static final WVWEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWDomainEventFactory();

	private final WVWMatch match;

	public DefaultWVWMatchScores(final WVWMatch match) {
		this.match = checkNotNull(match);
	}

	@Override
	protected void onChange(final int deltaRed, final int deltaGreen, final int deltaBlue) {
		final WVWMatchScoresChangedEvent event = WVW_MODEL_EVENT_FACTORY.newMatchScoresChangedEvent(this.createUnmodifiableReference(), deltaRed, deltaGreen, deltaBlue,
				this.match.createUnmodifiableReference());
		checkState(event != null);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to post new " + event);
		}
		this.getChannel().post(event);
	}

}
