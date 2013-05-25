package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IGuild;

class Guild implements IGuild{
	private final String id;
	public Guild(String id) {
		this.id = checkNotNull(id);
	}
	public String getId() {
		return this.id;
	}
}