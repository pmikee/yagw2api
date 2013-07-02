package de.justi.yagw2api.wrapper.model.wvw.types;

import de.justi.yagw2api.arenanet.dto.DTOConstants;

public enum WVWMapType implements IWVWMapType {
	CENTER, RED, GREEN, BLUE;

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
	public String getLabel() {
		return this.name();
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