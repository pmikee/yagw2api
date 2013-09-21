package de.justi.yagw2api.mumblelink.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-MumbleLink
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


import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.IMumbleLinkState;

abstract class AbstractForwardingMumbleLinkState implements IMumbleLinkState {
	@Nullable
	protected abstract IMumbleLinkState getForwardTo();

	@Override
	public final Optional<Integer> getUIVersion() {
		return this.getForwardTo() != null ? this.getForwardTo().getUIVersion() : Optional.<Integer> absent();
	}

	@Override
	public final Optional<Integer> getUITick() {
		return this.getForwardTo() != null ? this.getForwardTo().getUITick() : Optional.<Integer> absent();
	}

	@Override
	public final Optional<String> getGameName() {
		return this.getForwardTo() != null ? this.getForwardTo().getGameName() : Optional.<String> absent();
	}

	@Override
	public final Optional<String> getAvatarName() {
		return this.getForwardTo() != null ? this.getForwardTo().getAvatarName() : Optional.<String> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarPosition() {
		return this.getForwardTo() != null ? this.getForwardTo().getAvatarPosition() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarFront() {
		return this.getForwardTo() != null ? this.getForwardTo().getAvatarFront() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarTop() {
		return this.getForwardTo() != null ? this.getForwardTo().getAvatarTop() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraPosition() {
		return this.getForwardTo() != null ? this.getForwardTo().getCameraPosition() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraFront() {
		return this.getForwardTo() != null ? this.getForwardTo().getCameraFront() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraTop() {
		return this.getForwardTo() != null ? this.getForwardTo().getCameraTop() : Optional.<IMumbleLinkPosition> absent();
	}

	@Override
	public final Optional<Integer> getRegionId() {
		return this.getForwardTo() != null ? this.getForwardTo().getRegionId() : Optional.<Integer> absent();
	}

	@Override
	public final Optional<Integer> getBuild() {
		return this.getForwardTo() != null ? this.getForwardTo().getBuild() : Optional.<Integer> absent();
	}

	@Override
	public final Optional<Integer> getMapId() {
		return this.getForwardTo() != null ? this.getForwardTo().getMapId() : Optional.<Integer> absent();
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(AbstractForwardingMumbleLinkState.class).addValue(this.getForwardTo()).toString();
	}
}
