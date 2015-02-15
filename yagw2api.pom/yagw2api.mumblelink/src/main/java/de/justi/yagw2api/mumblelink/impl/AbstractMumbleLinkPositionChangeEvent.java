package de.justi.yagw2api.mumblelink.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-MumbleLink
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.IMumbleLinkPositionChangeEvent;
import de.justi.yagwapi.common.AbstractEvent;

abstract class AbstractMumbleLinkPositionChangeEvent extends AbstractEvent implements IMumbleLinkPositionChangeEvent {

	private final Optional<IMumbleLinkPosition> oldPosition;
	private final Optional<IMumbleLinkPosition> newPosition;

	protected AbstractMumbleLinkPositionChangeEvent(@Nullable IMumbleLinkPosition oldPosition, @Nullable IMumbleLinkPosition newPosition) {
		checkArgument(((oldPosition == null) ^ (newPosition == null)) || ((oldPosition != null) && !oldPosition.equals(newPosition)) || ((newPosition != null) && !newPosition.equals(oldPosition)),
				"Old position = %s and new position = %s have to be different.", oldPosition, newPosition);
		this.oldPosition = Optional.fromNullable(oldPosition);
		this.newPosition = Optional.fromNullable(newPosition);
	}

	@Override
	public Optional<IMumbleLinkPosition> getOldPosition() {
		return oldPosition;
	}

	@Override
	public Optional<IMumbleLinkPosition> getNewPosition() {
		return newPosition;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(super.toString()).add("old", this.oldPosition).add("new", this.newPosition).toString();
	}
}
