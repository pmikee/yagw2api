package yagw2api.server.character;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.server
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
import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;

@Immutable
public final class Avatar {

	// STATICS
	public static AvatarBuilder builder() {
		return new AvatarBuilder();
	}

	// EMBEDDED
	public static final class AvatarBuilder {
		// FIELDS
		@Nullable
		private String name = null;
		@Nullable
		private Integer worldId = null;
		@Nullable
		private Integer mapId = null;
		@Nullable
		private Boolean commander = null;
		@Nullable
		private Float xPosition = null;
		@Nullable
		private Float yPosition = null;
		@Nullable
		private Float zPosition = null;

		// CONSTRUCTOR
		private AvatarBuilder() {

		}

		// METHODS
		public AvatarBuilder name(final @Nullable String name) {
			this.name = name;
			return this;
		}

		public AvatarBuilder worldId(final @Nullable Integer worldId) {
			this.worldId = worldId;
			return this;
		}

		public AvatarBuilder mapId(final @Nullable Integer mapId) {
			this.mapId = mapId;
			return this;
		}

		public AvatarBuilder commander(final @Nullable Boolean commander) {
			this.commander = commander;
			return this;
		}

		public AvatarBuilder xPosition(final @Nullable Float position) {
			this.xPosition = position;
			return this;
		}

		public AvatarBuilder yPosition(final @Nullable Float position) {
			this.yPosition = position;
			return this;
		}

		public AvatarBuilder zPosition(final @Nullable Float position) {
			this.zPosition = position;
			return this;
		}

		public AvatarBuilder position(final @Nullable Float xPosition, final @Nullable Float yPosition, final @Nullable Float zPosition) {
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			this.zPosition = zPosition;
			return this;
		}

		public Avatar build() {
			return new Avatar(this);
		}

		@Override
		public final String toString() {
			return MoreObjects.toStringHelper(this).add("name", this.name).add("worldId", this.worldId).add("mapId", this.mapId).add("commander", this.commander)
					.add("x", this.xPosition).add("y", this.yPosition).add("z", this.zPosition).toString();
		}
	}

	// FIELDS

	@Expose
	private final LocalDateTime lastUpdated;
	@Expose
	private final String name;
	@Expose
	private final int worldId;
	@Expose
	private final int mapId;
	@Expose
	private final boolean commander;

	@Expose
	private final float xPosition;
	@Expose
	private final float yPosition;
	@Expose
	private final float zPosition;

	// CONSTRUCTOR
	/**
	 * default constructor
	 */
	@SuppressWarnings("unused")
	private Avatar() {
		this.lastUpdated = null;
		this.name = null;
		this.worldId = -1;
		this.mapId = -1;
		this.commander = false;
		this.xPosition = Float.MIN_VALUE;
		this.yPosition = Float.MIN_VALUE;
		this.zPosition = Float.MIN_VALUE;
	}

	public Avatar(final AvatarBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.lastUpdated = LocalDateTime.now();
		this.name = checkNotNull(builder.name, "missing characterName in %s", builder);
		this.worldId = checkNotNull(builder.worldId, "missing worldId in %s", builder);
		this.mapId = checkNotNull(builder.mapId, "missing mapId in %s", builder);
		this.commander = checkNotNull(builder.commander, "missing commander in %s", builder);
		this.xPosition = checkNotNull(builder.xPosition, "missing xPosition in %s", builder);
		this.yPosition = checkNotNull(builder.yPosition, "missing yPosition in %s", builder);
		this.zPosition = checkNotNull(builder.zPosition, "missing zPosition in %s", builder);

		checkArgument(this.worldId >= 0, "invalid worldId=%s in %s", builder.worldId, builder);
		checkArgument(this.mapId >= 0, "invalid mapId=%s in %s", builder.mapId, builder);
	}

	// METHODS

	public final LocalDateTime getLastUpdate() {
		return this.lastUpdated;
	}

	public final String getName() {
		return this.name;
	}

	public final AvatarBuilder setName(final String name) {
		checkNotNull(name, "missing name");
		return this.toBuilder().name(name);
	}

	public final int getWorldId() {
		return this.worldId;
	}

	public final AvatarBuilder setWorldId(final int id) {
		return this.toBuilder().worldId(id);
	}

	public final int getMapId() {
		return this.mapId;
	}

	public final AvatarBuilder setMapId(final int id) {
		return this.toBuilder().mapId(id);
	}

	public final boolean isCommander() {
		return this.commander;
	}

	public final AvatarBuilder setCommander(final boolean commander) {
		return this.toBuilder().commander(commander);
	}

	public final AvatarBuilder setPosition(final float xPosition, final float yPosition, final float zPosition) {
		return this.toBuilder().position(xPosition, yPosition, zPosition);
	}

	public final float getXPosition() {
		return this.xPosition;
	}

	public final AvatarBuilder setXPosition(final float xPosition) {
		return this.toBuilder().xPosition(xPosition);
	}

	public final float getYPosition() {
		return this.yPosition;
	}

	public final AvatarBuilder setYPosition(final float yPosition) {
		return this.toBuilder().yPosition(yPosition);
	}

	public final float getZPosition() {
		return this.zPosition;
	}

	public final AvatarBuilder setZPosition(final float zPosition) {
		return this.toBuilder().zPosition(zPosition);
	}

	public final AvatarBuilder toBuilder() {
		return builder().name(this.name).commander(this.commander).mapId(this.mapId).position(this.xPosition, this.yPosition, this.zPosition).worldId(this.worldId);
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this).add("name", this.name).add("worldId", this.worldId).add("mapId", this.mapId).add("commander", this.commander)
				.add("x", this.xPosition).add("y", this.yPosition).add("z", this.zPosition).toString();
	}
}
