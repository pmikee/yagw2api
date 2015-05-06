package de.justi.yagw2api.wrapper.map.domain.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import de.justi.yagw2api.wrapper.map.domain.MapFloor;

final class DefaultMapFloor implements MapFloor {
	// STATICS
	public static MapFloorBuilder builder() {
		return new DefaultMapFloorBuilder();
	}

	// EMBEDDED
	private static final class DefaultMapFloorBuilder implements MapFloor.MapFloorBuilder {
		// FIELDS
		@Nullable
		private Integer id = null;

		// CONSTRUCTOR
		private DefaultMapFloorBuilder() {
		}

		// METHODS
		@Override
		public MapFloor build() {
			return new DefaultMapFloor(this);
		}

		@Override
		public MapFloorBuilder index(final int index) {
			this.id = index;
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).toString();
		}
	}

	// FIELDS
	private final int id;

	// CONSTRUCTOR
	private DefaultMapFloor(final DefaultMapFloorBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
	}

	// METHODS
	@Override
	public int getIndex() {
		return this.id;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).addValue(this.id).toString();
	}
}
