package de.justi.yagw2api.wrapper.domain.map;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;

import de.justi.yagwapi.common.Tuple2;

final class DefaultContinent implements Continent {

	// STATIC
	public static ContinentBuilder builder() {
		return new DefaultContinentBuilder();
	}

	// EMBEDDED
	public static final class DefaultContinentBuilder implements Continent.ContinentBuilder {
		// FIELDS
		@Nullable
		private String id = null;
		@Nullable
		private String name = null;
		@Nullable
		private Tuple2<Integer, Integer> dimension = null;

		// CONSTRUCTOR
		private DefaultContinentBuilder() {

		}

		// METHODS
		@Override
		public DefaultContinent build() {
			return new DefaultContinent(this);
		}

		@Override
		public ContinentBuilder name(@Nullable final String name) {
			this.name = name;
			return this;
		}

		@Override
		public ContinentBuilder id(@Nullable final String id) {
			this.id = id;
			return this;
		}

		@Override
		public ContinentBuilder dimension(@Nullable final Tuple2<Integer, Integer> dimension) {
			this.dimension = dimension;
			return this;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).toString();
		}

	}

	// FIELDS
	private final String id;
	private final String name;
	private final Tuple2<Integer, Integer> dimension;

	// CONSTRUCTOR
	private DefaultContinent(final DefaultContinentBuilder builder) {
		checkNotNull(builder, "missing builder");
		this.id = checkNotNull(builder.id, "missing id in %s", builder);
		this.name = checkNotNull(builder.name, "missing name in %s", builder);
		this.dimension = checkNotNull(builder.dimension, "missing dimension in %s", builder);
	}

	// METHODS

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Tuple2<Integer, Integer> getDimension() {
		return this.dimension;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", this.id).add("name", this.name).add("dimension", this.dimension).toString();
	}

}
