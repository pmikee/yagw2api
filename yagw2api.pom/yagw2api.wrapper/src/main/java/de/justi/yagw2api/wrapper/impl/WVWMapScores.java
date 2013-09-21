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

import org.apache.log4j.Logger;

import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWModelEventFactory;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;

final class WVWMapScores extends AbstractWVWScores {
	private static final Logger LOGGER = Logger.getLogger(WVWMapScores.class);
	private static final IWVWModelEventFactory WVW_MODEL_EVENT_FACTORY = YAGW2APIWrapper.INSTANCE.getWVWModelEventFactory();

	private final IWVWMap map;

	public WVWMapScores(IWVWMap map) {
		this.map = checkNotNull(map);
	}

	@Override
	protected void onChange(int deltaRed, int deltaGreen, int deltaBlue) {
		final IWVWMapScoresChangedEvent event = WVW_MODEL_EVENT_FACTORY.newMapScoresChangedEvent(this.createUnmodifiableReference(), deltaRed, deltaGreen, deltaBlue,
				this.map.createUnmodifiableReference());
		checkState(event != null);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Going to post new " + event);
		}
		this.getChannel().post(event);
	}
}
