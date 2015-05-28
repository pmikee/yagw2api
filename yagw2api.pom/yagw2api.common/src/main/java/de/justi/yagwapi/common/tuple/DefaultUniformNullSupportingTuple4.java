package de.justi.yagwapi.common.tuple;

import javax.annotation.Nullable;

final class DefaultUniformNullSupportingTuple4<V> extends AbstractTuple4<V, V, V, V> implements UniformTuple4<V> {

	DefaultUniformNullSupportingTuple4(@Nullable final V value1, @Nullable final V value2, @Nullable final V value3, @Nullable final V value4) {
		super(value1, value2, value3, value4);
	}

}
