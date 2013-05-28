package de.justi.yagw2api.wrapper.model.wvw;

import de.justi.yagw2api.wrapper.model.wvw.types.IWVWLocationType;

public interface IHasWVWLocation<I extends IHasWVWLocation<?>> {
	IWVWLocationType getLocation();
	I createImmutableReference();
}
