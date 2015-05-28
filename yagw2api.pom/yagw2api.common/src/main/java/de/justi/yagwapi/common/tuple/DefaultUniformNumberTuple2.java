package de.justi.yagwapi.common.tuple;

import javax.annotation.Nullable;

final class DefaultUniformNumberTuple2<V extends Number> extends AbstractTuple2<V, V> implements UniformNumberTuple2<V> {

	DefaultUniformNumberTuple2(@Nullable final V value1, @Nullable final V value2) {
		super(value1, value2);
	}

}