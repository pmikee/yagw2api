package de.justi.yagwapi.common.tuple;

import javax.annotation.Nullable;

final class DefaultUniformNullSupportingTuple3<V> extends AbstractTuple3<V, V, V> implements UniformTuple3<V> {

	DefaultUniformNullSupportingTuple3(@Nullable final V value1, @Nullable final V value2, @Nullable final V value3) {
		super(value1, value2, value3);
	}

}
