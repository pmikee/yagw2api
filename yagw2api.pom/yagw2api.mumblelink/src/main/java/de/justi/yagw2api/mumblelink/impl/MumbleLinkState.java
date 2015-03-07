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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.sun.jna.Pointer;

import de.justi.yagw2api.mumblelink.IMumbleLinkAvatar;
import de.justi.yagw2api.mumblelink.IMumbleLinkPosition;
import de.justi.yagw2api.mumblelink.IMumbleLinkState;

final class MumbleLinkState implements IMumbleLinkState {
	private static final Logger LOGGER = LoggerFactory.getLogger(MumbleLinkState.class);

	@Nullable
	public static final IMumbleLinkState of(final Pointer memMapFileData) {
		checkNotNull(memMapFileData);
		final Integer uiVersion = memMapFileData.getInt(0);
		if ((uiVersion == null) || (uiVersion < 0)) {
			LOGGER.trace("Invalid uiVersion={}", uiVersion);
			return null;
		}
		final Integer uiTick = memMapFileData.getInt(4);
		if ((uiTick == null) || (uiTick < 0)) {
			LOGGER.trace("Invalid uiTick={}", uiTick);
			return null;
		}
		final Optional<IMumbleLinkPosition> avatarPosition = MumbleLinkPosition.of(memMapFileData.getFloatArray(8, 3));
		if (!avatarPosition.isPresent()) {
			LOGGER.trace("Invalid avatarPosition={}", avatarPosition);
			return null;
		}
		final Optional<IMumbleLinkPosition> avatarFront = MumbleLinkPosition.of(memMapFileData.getFloatArray(20, 3));
		if (!avatarFront.isPresent()) {
			LOGGER.trace("Invalid avatarFront={}", avatarFront);
			return null;
		}
		final Optional<IMumbleLinkPosition> avatarTop = MumbleLinkPosition.of(memMapFileData.getFloatArray(32, 3));
		if (!avatarTop.isPresent()) {
			LOGGER.trace("Invalid avatarTop={}", avatarTop);
			return null;
		}
		final Optional<IMumbleLinkPosition> cameraPosition = MumbleLinkPosition.of(memMapFileData.getFloatArray(556, 3));
		if (!cameraPosition.isPresent()) {

			LOGGER.trace("Invalid cameraPosition={}", cameraPosition);
			return null;
		}
		final Optional<IMumbleLinkPosition> cameraFront = MumbleLinkPosition.of(memMapFileData.getFloatArray(568, 3));
		if (!cameraFront.isPresent()) {
			LOGGER.trace("Invalid cameraFront={]", cameraFront);
			return null;
		}
		final Optional<IMumbleLinkPosition> cameraTop = MumbleLinkPosition.of(memMapFileData.getFloatArray(580, 3));
		if (!cameraTop.isPresent()) {
			LOGGER.trace("Invalid cameraTop={}", cameraTop);
			return null;
		}
		final String avatarJSON = String.copyValueOf(memMapFileData.getCharArray(592, 256)).trim();
		if ((avatarJSON == null) || (avatarJSON.length() == 0)) {
			LOGGER.trace("Invalid avatarJSON={}", avatarJSON);
			return null;
		}
		final IMumbleLinkAvatar avatar = MumbleLinkAvatar.of(avatarJSON);
		final String gameName = String.copyValueOf(memMapFileData.getCharArray(44, 256)).trim();
		if ((gameName == null) || (!"Guild Wars 2".equals(gameName))) {
			LOGGER.trace("Invalid gameName={}", gameName);
			return null;
		}
		final Integer contextLength = memMapFileData.getInt(1104);
		if ((contextLength == null) || (contextLength < 0)) {
			LOGGER.trace("Invalid contextLength={}", contextLength);
			return null;
		}

		final byte[] context = memMapFileData.getByteArray(1108, 256);
		if (context == null) {
			LOGGER.trace("Invalid context={}", Arrays.toString(context));
			return null;
		}

		final int regionId = memMapFileData.getInt(1108 + 0); // affirmed
		final int ip1 = unsignedByteValue2ByteValue(memMapFileData.getByte(1108 + 4));
		final int ip2 = unsignedByteValue2ByteValue(memMapFileData.getByte(1108 + 5));
		final int ip3 = unsignedByteValue2ByteValue(memMapFileData.getByte(1108 + 6));
		final int ip4 = unsignedByteValue2ByteValue(memMapFileData.getByte(1108 + 7));
		@SuppressWarnings("unused")
		final String serverIp = ip1 + "." + ip2 + "." + ip3 + "." + ip4; // affirmed
		final int build = memMapFileData.getInt(1108 + 44); // affirmed
		final int mapId = memMapFileData.getInt(1108 + 28); // affirmed

		return new MumbleLinkState(uiVersion, uiTick, avatarPosition.get(), avatarFront.get(), avatarTop.get(), cameraPosition.get(), cameraFront.get(), cameraTop.get(), avatar,
				gameName, regionId, build, mapId);
	}

	private static int unsignedByteValue2ByteValue(final int unsignedByteValue) {
		return (256 + unsignedByteValue) % 256;
	}

	private final Optional<Integer> uiVersion;
	private final Optional<Integer> uiTick;
	private final Optional<IMumbleLinkPosition> avatarPosition;
	private final Optional<IMumbleLinkPosition> avatarFront;
	private final Optional<IMumbleLinkPosition> avatarTop;
	private final Optional<IMumbleLinkPosition> cameraPosition;
	private final Optional<IMumbleLinkPosition> cameraFront;
	private final Optional<IMumbleLinkPosition> cameraTop;
	private final Optional<IMumbleLinkAvatar> avatar;
	private final Optional<String> gameName;
	private final Optional<Integer> regionId;
	private final Optional<Integer> build;
	private final Optional<Integer> mapId;

	private MumbleLinkState(final int uiVersion, final int uiTick, final IMumbleLinkPosition avatarPosition, final IMumbleLinkPosition avatarFront,
			final IMumbleLinkPosition avatarTop, final IMumbleLinkPosition cameraPosition, final IMumbleLinkPosition cameraFront, final IMumbleLinkPosition cameraTop,
			final IMumbleLinkAvatar avatar, final String gameName, final int regionId, final int build, final int mapId) {
		checkNotNull(avatarPosition);
		checkNotNull(avatarFront);
		checkNotNull(avatarTop);
		checkNotNull(cameraPosition);
		checkNotNull(cameraFront);
		checkNotNull(cameraTop);
		checkNotNull(avatar);
		checkNotNull(gameName);

		this.uiVersion = Optional.of(uiVersion);
		this.uiTick = Optional.of(uiTick);
		this.avatarPosition = Optional.of(avatarPosition);
		this.avatarFront = Optional.of(avatarFront);
		this.avatarTop = Optional.of(avatarTop);
		this.cameraPosition = Optional.of(cameraPosition);
		this.cameraFront = Optional.of(cameraFront);
		this.cameraTop = Optional.of(cameraTop);
		this.avatar = Optional.of(avatar);
		this.gameName = Optional.of(gameName);
		this.regionId = Optional.of(regionId);
		this.build = Optional.of(build);
		this.mapId = Optional.of(mapId);
	}

	@Override
	public final Optional<Integer> getUIVersion() {
		return this.uiVersion;
	}

	@Override
	public final Optional<Integer> getUITick() {
		return this.uiTick;
	}

	@Override
	public final Optional<String> getGameName() {
		return this.gameName;
	}

	@Override
	public final Optional<IMumbleLinkAvatar> getAvatar() {
		return this.avatar;
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarPosition() {
		return this.avatarPosition;
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarFront() {
		return this.avatarFront;
	}

	@Override
	public final Optional<IMumbleLinkPosition> getAvatarTop() {
		return this.avatarTop;
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraPosition() {
		return this.cameraPosition;
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraFront() {
		return this.cameraFront;
	}

	@Override
	public final Optional<IMumbleLinkPosition> getCameraTop() {
		return this.cameraTop;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.avatar == null) ? 0 : this.avatar.hashCode());
		result = prime * result + ((this.avatarFront == null) ? 0 : this.avatarFront.hashCode());
		result = prime * result + ((this.avatarPosition == null) ? 0 : this.avatarPosition.hashCode());
		result = prime * result + ((this.avatarTop == null) ? 0 : this.avatarTop.hashCode());
		result = prime * result + ((this.build == null) ? 0 : this.build.hashCode());
		result = prime * result + ((this.cameraFront == null) ? 0 : this.cameraFront.hashCode());
		result = prime * result + ((this.cameraPosition == null) ? 0 : this.cameraPosition.hashCode());
		result = prime * result + ((this.cameraTop == null) ? 0 : this.cameraTop.hashCode());
		result = prime * result + ((this.gameName == null) ? 0 : this.gameName.hashCode());
		result = prime * result + ((this.mapId == null) ? 0 : this.mapId.hashCode());
		result = prime * result + ((this.regionId == null) ? 0 : this.regionId.hashCode());
		result = prime * result + ((this.uiTick == null) ? 0 : this.uiTick.hashCode());
		result = prime * result + ((this.uiVersion == null) ? 0 : this.uiVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		MumbleLinkState other = (MumbleLinkState) obj;
		if (this.avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!this.avatar.equals(other.avatar))
			return false;
		if (this.avatarFront == null) {
			if (other.avatarFront != null)
				return false;
		} else if (!this.avatarFront.equals(other.avatarFront))
			return false;
		if (this.avatarPosition == null) {
			if (other.avatarPosition != null)
				return false;
		} else if (!this.avatarPosition.equals(other.avatarPosition))
			return false;
		if (this.avatarTop == null) {
			if (other.avatarTop != null)
				return false;
		} else if (!this.avatarTop.equals(other.avatarTop))
			return false;
		if (this.build == null) {
			if (other.build != null)
				return false;
		} else if (!this.build.equals(other.build))
			return false;
		if (this.cameraFront == null) {
			if (other.cameraFront != null)
				return false;
		} else if (!this.cameraFront.equals(other.cameraFront))
			return false;
		if (this.cameraPosition == null) {
			if (other.cameraPosition != null)
				return false;
		} else if (!this.cameraPosition.equals(other.cameraPosition))
			return false;
		if (this.cameraTop == null) {
			if (other.cameraTop != null)
				return false;
		} else if (!this.cameraTop.equals(other.cameraTop))
			return false;
		if (this.gameName == null) {
			if (other.gameName != null)
				return false;
		} else if (!this.gameName.equals(other.gameName))
			return false;
		if (this.mapId == null) {
			if (other.mapId != null)
				return false;
		} else if (!this.mapId.equals(other.mapId))
			return false;
		if (this.regionId == null) {
			if (other.regionId != null)
				return false;
		} else if (!this.regionId.equals(other.regionId))
			return false;
		if (this.uiTick == null) {
			if (other.uiTick != null)
				return false;
		} else if (!this.uiTick.equals(other.uiTick))
			return false;
		if (this.uiVersion == null) {
			if (other.uiVersion != null)
				return false;
		} else if (!this.uiVersion.equals(other.uiVersion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("uiVersion", this.uiVersion).add("uiTick", this.uiTick).add("avatarPosition", this.avatarPosition)
				.add("avatarFront", this.avatarFront).add("avatarTop", this.avatarTop).add("cameraPosition", this.cameraPosition).add("cameraFront", this.cameraFront)
				.add("cameraTop", this.cameraTop).add("avatar", this.avatar).add("gameName", this.gameName).add("regionId", this.regionId).add("build", this.build)
				.add("mapId", this.mapId).toString();
	}

	@Override
	public Optional<Integer> getRegionId() {
		return this.regionId;
	}

	@Override
	public Optional<Integer> getBuild() {
		return this.build;
	}

	@Override
	public Optional<Integer> getMapId() {
		return this.mapId;
	}
}
