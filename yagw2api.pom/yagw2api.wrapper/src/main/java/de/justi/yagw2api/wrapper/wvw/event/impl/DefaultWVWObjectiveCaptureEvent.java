package de.justi.yagw2api.wrapper.wvw.event.impl;

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

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.world.domain.World;
import de.justi.yagw2api.wrapper.wvw.domain.WVWObjective;
import de.justi.yagw2api.wrapper.wvw.event.WVWObjectiveCaptureEvent;

final class DefaultWVWObjectiveCaptureEvent extends AbstractWVWObjectiveEvent implements WVWObjectiveCaptureEvent {

	private final World newOwningWorld;
	private final Optional<World> previousOwningWorld;

	public DefaultWVWObjectiveCaptureEvent(final WVWObjective source, final World newOwningWorld) {
		this(checkNotNull(source), checkNotNull(newOwningWorld), null);
	}

	public DefaultWVWObjectiveCaptureEvent(final WVWObjective source, final World newOwningWorld, final World previousOwningWorld) {
		super(checkNotNull(source));
		this.newOwningWorld = checkNotNull(newOwningWorld);
		this.previousOwningWorld = Optional.fromNullable(previousOwningWorld);
	}

	@Override
	public World getNewOwningWorld() {
		return this.newOwningWorld;
	}

	@Override
	public Optional<World> getPreviousOwningWorld() {
		return this.previousOwningWorld;
	}

	@Override
	public String toString() {
		final com.google.common.base.MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("super", super.toString());
		helper.add("objective", this.getObjective());
		if (this.getObjective().getMap().isPresent()) {
			helper.add("mapType", this.getObjective().getMap().get().getType());
			if (this.getObjective().getMap().get().getMatch().isPresent()) {
				helper.add("matchId", this.getObjective().getMap().get().getMatch().get().getId());
			}
		}
		helper.add("capturedBy", this.newOwningWorld);
		if (this.previousOwningWorld.isPresent()) {
			helper.add("from", this.previousOwningWorld.get());
		}
		return helper.toString();
	}
}
