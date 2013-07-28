package de.justi.yagw2api.wrapper;

import java.util.Locale;

import com.google.common.base.Optional;

import de.justi.yagwapi.common.IImmutable;

public interface IWVWMapType extends IImmutable {
	Optional<String> getLabel(Locale locale);

	boolean isCenter();

	boolean isRed();

	boolean isGreen();

	boolean isBlue();
}
