package de.justi.yagw2api.wrapper.domain.map;

import de.justi.yagwapi.common.Tuple2;

public interface Continent {

	static interface ContinentBuilder {
		Continent build();

		ContinentBuilder name(String name);

		ContinentBuilder id(String id);

		ContinentBuilder dimension(Tuple2<Integer, Integer> dimension);
	}

	String getId();

	String getName();

	Tuple2<Integer, Integer> getDimension();
}
