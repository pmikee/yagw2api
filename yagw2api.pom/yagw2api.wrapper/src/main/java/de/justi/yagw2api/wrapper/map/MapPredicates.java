package de.justi.yagw2api.wrapper.map;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Predicate;

import de.justi.yagw2api.wrapper.map.domain.Continent;

public final class MapPredicates {
	// STATICS

	public static Predicate<Continent> continentIdEquals(final String id) {
		checkNotNull(id, "missing id");
		return new Predicate<Continent>() {
			@Override
			public boolean apply(final Continent input) {
				checkNotNull(input, "missing input");
				return input.getId().equals(id);
			}
		};
	}

	// EMBEDDED

	// CONSTRUCTOR
	private MapPredicates() {
		throw new AssertionError("no instance");
	}
}
