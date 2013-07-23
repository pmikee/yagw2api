package de.justi.yagw2api.wrapper;


public interface IHasWVWLocation<I extends IHasWVWLocation<?>> {
	IWVWLocationType getLocation();
	I createUnmodifiableReference();
}
