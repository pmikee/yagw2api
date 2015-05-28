package de.justi.yagwapi.common.tuple;

import static com.google.common.base.Preconditions.checkNotNull;

final class DefaultUniformNumberTuple3<V extends Number> extends AbstractTuple3<V, V, V> implements UniformNumberTuple3<V> {

	DefaultUniformNumberTuple3(final V value1, final V value2, final V value3) {
		super(checkNotNull(value1, "missing value1"), checkNotNull(value2, "missing value2"), checkNotNull(value3, "missing value3"));
	}

}