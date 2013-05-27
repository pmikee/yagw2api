package de.justi.gw2.model.wvw;

import de.justi.gw2.model.wvw.types.IWVWLocationType;

public interface IHasWVWLocation<I extends IHasWVWLocation<?>> {
	IWVWLocationType getLocation();
	I createImmutableReference();
}
