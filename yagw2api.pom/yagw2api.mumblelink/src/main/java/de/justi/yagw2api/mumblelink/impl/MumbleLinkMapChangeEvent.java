package de.justi.yagw2api.mumblelink.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkMapChangeEvent;
import de.justi.yagwapi.common.AbstractEvent;

final class MumbleLinkMapChangeEvent extends AbstractEvent implements IMumbleLinkMapChangeEvent {
	private final Optional<Integer> oldMapId;
	private final Optional<Integer> newMapId;

	public MumbleLinkMapChangeEvent(@Nullable final Integer oldMapId, @Nullable final Integer newMapId) {
		this.oldMapId = Optional.fromNullable(oldMapId);
		this.newMapId = Optional.fromNullable(newMapId);
	}

	@Override
	public Optional<Integer> getOldMapId() {
		return this.oldMapId;
	}

	@Override
	public Optional<Integer> getNewMapId() {
		return this.newMapId;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("oldMapId", this.oldMapId).add("newMapId", this.newMapId);
	}
}
