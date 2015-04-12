package de.justi.yagw2api.wrapper.domain.wvw.event;

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
import de.justi.yagw2api.wrapper.domain.wvw.IWVWMap;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWObjective;
import de.justi.yagwapi.common.AbstractEvent;

abstract class AbstractWVWObjectiveEvent extends AbstractEvent implements IWVWObjectiveEvent {
	private final IWVWObjective source;

	public AbstractWVWObjectiveEvent(final IWVWObjective source) {
		super();
		this.source = checkNotNull(source);
	}

	@Override
	public IWVWObjective getObjective() {
		return this.source;
	}

	@Override
	public IWVWMap getMap() {
		checkState(this.getObjective().getMap().isPresent());
		return this.getObjective().getMap().get();
	}
}
