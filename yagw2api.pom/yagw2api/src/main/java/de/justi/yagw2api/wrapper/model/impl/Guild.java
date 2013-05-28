package de.justi.yagw2api.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import de.justi.yagw2api.wrapper.model.IGuild;


class Guild implements IGuild{
	private final String id;
	public Guild(String id) {
		this.id = checkNotNull(id);
	}
	public String getId() {
		return this.id;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).toString();
	}
}