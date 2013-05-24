package model.impl;

import model.IWVWLocationType;

enum WVWLocationType implements IWVWLocationType{
	RED_HOMELANDS_RED_KEEP,
	RED_HOMELANDS_GREEN_KEEP,
	RED_HOMELANDS_BLUE_KEEP;

	public String getLabel() {
		return this.name();
	}
}