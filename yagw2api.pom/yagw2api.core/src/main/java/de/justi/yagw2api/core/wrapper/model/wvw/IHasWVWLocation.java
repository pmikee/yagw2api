package de.justi.yagw2api.core.wrapper.model.wvw;

import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWLocationType;

public interface IHasWVWLocation<I extends IHasWVWLocation<?>> {
	IWVWLocationType getLocation();
	I createImmutableReference();
}
