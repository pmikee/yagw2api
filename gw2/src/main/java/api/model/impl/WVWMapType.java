package api.model.impl;

import api.model.IWVWMapType;

enum WVWMapType implements IWVWMapType {
	CENTER,
	RED,
	GREEN,
	BLUE;

	public String getLabel() {
		return this.name();
	}
}