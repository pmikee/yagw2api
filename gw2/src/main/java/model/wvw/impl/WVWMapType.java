package model.wvw.impl;

import model.wvw.IWVWMapType;

enum WVWMapType implements IWVWMapType {
	CENTER,
	RED,
	GREEN,
	BLUE;

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