package de.justi.yagw2api.wrapper.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.DTOConstants;
import de.justi.yagw2api.wrapper.IWVWMapType;

public enum WVWMapType implements IWVWMapType {
	CENTER, RED, GREEN, BLUE;

	private static final String BUNDLE_BASENAME = "maptypes";
	private static final Logger LOGGER = Logger.getLogger(WVWMapType.class);

	public static WVWMapType fromDTOString(String dtoString) {
		switch (dtoString.toUpperCase()) {
			case DTOConstants.CENTER_MAP_TYPE_STRING:
				return CENTER;
			case DTOConstants.BLUE_MAP_TYPE_STRING:
				return BLUE;
			case DTOConstants.GREEN_MAP_TYPE_STRING:
				return GREEN;
			case DTOConstants.RED_MAP_TYPE_STRING:
				return RED;
			default:
				throw new IllegalArgumentException("Unknown dtoString: " + dtoString);
		}
	}

	@Override
	public Optional<String> getLabel(Locale locale) {
		checkNotNull(locale);
		final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASENAME, locale);
		try {
			return Optional.of(bundle.getString(this.name()));
		} catch (MissingResourceException e) {
			LOGGER.error("Missing translation of " + this.name() + " for " + locale, e);
			return Optional.absent();
		}
	}

	@Override
	public boolean isCenter() {
		return this.equals(CENTER);
	}

	@Override
	public boolean isRed() {
		return this.equals(RED);
	}

	@Override
	public boolean isGreen() {
		return this.equals(GREEN);
	}

	@Override
	public boolean isBlue() {
		return this.equals(BLUE);
	}
}