package de.justi.yagwapi.common.tuple;

import javax.annotation.Nullable;

final class DefaultUniformNullSupportingTuple5<V> extends AbstractTuple5<V, V, V, V, V> implements UniformTuple5<V> {

	DefaultUniformNullSupportingTuple5(@Nullable final V value1, @Nullable final V value2, @Nullable final V value3, @Nullable final V value4, @Nullable final V value5) {
		super(value1, value2, value3, value4, value5);
	}

}
