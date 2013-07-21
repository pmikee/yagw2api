package de.justi.yagw2api.mumblelink.impl;

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
