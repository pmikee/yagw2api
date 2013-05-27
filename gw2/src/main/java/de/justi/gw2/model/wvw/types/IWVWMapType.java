package de.justi.gw2.model.wvw.types;

import de.justi.gw2.model.IImmutable;

public interface IWVWMapType extends IImmutable {
	String getLabel();

	boolean isCenter();

	boolean isRed();

	boolean isGreen();

	boolean isBlue();
}
