package de.justi.yagw2api.wrapper;

import de.justi.yagwapi.common.IImmutable;

public interface IWVWMapType extends IImmutable {
	String getLabel();

	boolean isCenter();

	boolean isRed();

	boolean isGreen();

	boolean isBlue();
}
