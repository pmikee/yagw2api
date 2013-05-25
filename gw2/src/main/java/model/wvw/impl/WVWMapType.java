package model.wvw.impl;

import model.wvw.IWVWMapType;
import api.dto.IWVWMatchDetailsDTO;

enum WVWMapType implements IWVWMapType {
	CENTER,
	RED,
	GREEN,
	BLUE;
	
	public static WVWMapType fromDTOString(String dtoString) {
		switch(dtoString.toUpperCase()) {
			case IWVWMatchDetailsDTO.CENTER_MAP_TYPE_STRING:
				return CENTER;
			case IWVWMatchDetailsDTO.BLUE_MAP_TYPE_STRING:
				return BLUE;
			case IWVWMatchDetailsDTO.GREEN_MAP_TYPE_STRING:
				return GREEN;
			case IWVWMatchDetailsDTO.RED_MAP_TYPE_STRING:
				return RED;
			default:
				throw new IllegalArgumentException("Unknown dtoString: "+dtoString);
		}
	}
	
	public String getLabel() {
		return this.name();
	}

	public boolean isCenter() {
		return this.equals(CENTER);
	}

	public boolean isRed() {
		return this.equals(RED);
	}

	public boolean isGreen() {
		return this.equals(GREEN);
	}

	public boolean isBlue() {
		return this.equals(BLUE);
	}
}