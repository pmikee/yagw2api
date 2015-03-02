package yagw2api.server.character;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;

@Immutable
public final class Character {

	// STATICS
	public static CharacterBuilder builder() {
		return new CharacterBuilder();
	}

	// EMBEDDED
	public static final class CharacterBuilder {
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
		private CharacterBuilder() {

		}

		// METHODS
		public CharacterBuilder name(final @Nullable String name) {
			this.name = name;
			return this;
		}

		public CharacterBuilder worldId(final @Nullable Integer worldId) {
			this.worldId = worldId;
			return this;
		}

		public CharacterBuilder mapId(final @Nullable Integer mapId) {
			this.mapId = mapId;
			return this;
		}

		public CharacterBuilder commander(final @Nullable Boolean commander) {
			this.commander = commander;
			return this;
		}

		public CharacterBuilder xPosition(final @Nullable Float position) {
			this.xPosition = position;
			return this;
		}

		public CharacterBuilder yPosition(final @Nullable Float position) {
			this.yPosition = position;
			return this;
		}

		public CharacterBuilder zPosition(final @Nullable Float position) {
			this.zPosition = position;
			return this;
		}

		public CharacterBuilder position(final @Nullable Float xPosition, final @Nullable Float yPosition, final @Nullable Float zPosition) {
			this.xPosition = xPosition;
			this.yPosition = yPosition;
			this.zPosition = zPosition;
			return this;
		}

		public Character build() {
			return new Character(this);
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

	public Character(final CharacterBuilder builder) {
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

	public final CharacterBuilder setName(final String name) {
		checkNotNull(name, "missing name");
		return this.toBuilder().name(name);
	}

	public final int getWorldId() {
		return this.worldId;
	}

	public final CharacterBuilder setWorldId(final int id) {
		return this.toBuilder().worldId(id);
	}

	public final int getMapId() {
		return this.mapId;
	}

	public final CharacterBuilder setMapId(final int id) {
		return this.toBuilder().mapId(id);
	}

	public final boolean isCommander() {
		return this.commander;
	}

	public final CharacterBuilder setCommander(final boolean commander) {
		return this.toBuilder().commander(commander);
	}

	public final CharacterBuilder setPosition(final float xPosition, final float yPosition, final float zPosition) {
		return this.toBuilder().position(xPosition, yPosition, zPosition);
	}

	public final float getXPosition() {
		return this.xPosition;
	}

	public final CharacterBuilder setXPosition(final float xPosition) {
		return this.toBuilder().xPosition(xPosition);
	}

	public final float getYPosition() {
		return this.yPosition;
	}

	public final CharacterBuilder setYPosition(final float yPosition) {
		return this.toBuilder().yPosition(yPosition);
	}

	public final float getZPosition() {
		return this.zPosition;
	}

	public final CharacterBuilder setZPosition(final float zPosition) {
		return this.toBuilder().zPosition(zPosition);
	}

	public final CharacterBuilder toBuilder() {
		return builder().name(this.name).commander(this.commander).mapId(this.mapId).position(this.xPosition, this.yPosition, this.zPosition).worldId(this.worldId);
	}

	@Override
	public final String toString() {
		return MoreObjects.toStringHelper(this).add("name", this.name).add("worldId", this.worldId).add("mapId", this.mapId).add("commander", this.commander)
				.add("x", this.xPosition).add("y", this.yPosition).add("z", this.zPosition).toString();
	}
}
