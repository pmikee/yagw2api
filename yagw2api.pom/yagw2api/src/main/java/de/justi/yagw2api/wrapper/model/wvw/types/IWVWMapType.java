package de.justi.yagw2api.wrapper.model.wvw.types;

import de.justi.yagw2api.wrapper.model.IImmutable;

public interface IWVWMapType extends IImmutable {
	String getLabel();

	boolean isCenter();

	boolean isRed();

	boolean isGreen();

	boolean isBlue();
}
