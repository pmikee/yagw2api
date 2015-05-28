package de.justi.yagwapi.common.tuple;

import javax.annotation.Nullable;

final class DefaultUniformNullSupportingTuple2<V> extends AbstractTuple2<V, V> implements UniformTuple2<V> {

	DefaultUniformNullSupportingTuple2(@Nullable final V value1, @Nullable final V value2) {
		super(value1, value2);
	}

}
