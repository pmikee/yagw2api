package de.justi.yagw2api.mumblelink.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-MumbleLink
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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

import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;
import de.justi.yagwapi.common.AbstractEvent;

final class MumbleLinkAvatarChangeEvent extends AbstractEvent implements IMumbleLinkAvatarChangeEvent {
	private final Optional<String> oldAvatarName;
	private final Optional<String> newAvatarName;

	public MumbleLinkAvatarChangeEvent(@Nullable String oldAvatarName, @Nullable String newAvatarName) {
		checkArgument(
				((oldAvatarName == null) ^ (newAvatarName == null)) || ((oldAvatarName != null) && !oldAvatarName.equals(newAvatarName))
						|| ((newAvatarName != null) && !newAvatarName.equals(oldAvatarName)), "Old avatar name = '%s' and new avatar name = '%s' have to be different.", oldAvatarName, newAvatarName);
		this.oldAvatarName = Optional.fromNullable(oldAvatarName);
		this.newAvatarName = Optional.fromNullable(newAvatarName);
	}

	@Override
	public Optional<String> getOldAvatarName() {
		return this.oldAvatarName;
	}

	@Override
	public Optional<String> getNewAvatarName() {
		return this.newAvatarName;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(super.toString()).add("old", this.oldAvatarName).add("new", this.newAvatarName).toString();
	}
}
