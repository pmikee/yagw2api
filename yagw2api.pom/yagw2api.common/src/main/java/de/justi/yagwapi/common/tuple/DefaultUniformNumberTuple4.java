package de.justi.yagwapi.common.tuple;

import static com.google.common.base.Preconditions.checkNotNull;

final class DefaultUniformNumberTuple4<V extends Number> extends AbstractTuple4<V, V, V, V> implements UniformNumberTuple4<V> {

	DefaultUniformNumberTuple4(final V value1, final V value2, final V value3, final V value4) {
		super(checkNotNull(value1, "missing value1"), checkNotNull(value2, "missing value2"), checkNotNull(value3, "missing value3"), checkNotNull(value4, "missing value4"));
	}

}