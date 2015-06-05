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

import static com.google.common.base.Preconditions.checkArgument;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Optional;

import de.justi.yagw2api.common.event.AbstractEvent;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatar;
import de.justi.yagw2api.mumblelink.IMumbleLinkAvatarChangeEvent;

final class MumbleLinkAvatarChangeEvent extends AbstractEvent implements IMumbleLinkAvatarChangeEvent {
	private final Optional<IMumbleLinkAvatar> oldAvatar;
	private final Optional<IMumbleLinkAvatar> newAvatar;

	public MumbleLinkAvatarChangeEvent(@Nullable final IMumbleLinkAvatar old, @Nullable final IMumbleLinkAvatar newAvatar) {
		checkArgument(((old == null) ^ (newAvatar == null)) || ((old != null) && !old.equals(newAvatar)) || ((newAvatar != null) && !newAvatar.equals(old)),
				"Old avatar '%s' and new avatar'%s' have to be different.", old, newAvatar);
		this.oldAvatar = Optional.fromNullable(old);
		this.newAvatar = Optional.fromNullable(newAvatar);
	}

	@Override
	public Optional<IMumbleLinkAvatar> getOldAvatar() {
		return this.oldAvatar;
	}

	@Override
	public Optional<IMumbleLinkAvatar> getNewAvatar() {
		return this.newAvatar;
	}

	@Override
	protected ToStringHelper toStringHelper() {
		return super.toStringHelper().add("old", this.oldAvatar).add("new", this.newAvatar);
	}
}
