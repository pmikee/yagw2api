package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.wvw.IWVWMapType;

enum WVWMapType implements IWVWMapType {
	CENTER,
	RED,
	GREEN,
	BLUE;

	public static WVWMapType fromDTOString(String dtoString) {
		return WVWMapType.valueOf(checkNotNull(dtoString).toUpperCase());
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