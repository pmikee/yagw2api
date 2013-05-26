package model.wvw;

import model.wvw.types.IWVWLocationType;

public interface IHasWVWLocation<I extends IHasWVWLocation<?>> {
	IWVWLocationType getLocation();
	I createImmutableReference();
}
