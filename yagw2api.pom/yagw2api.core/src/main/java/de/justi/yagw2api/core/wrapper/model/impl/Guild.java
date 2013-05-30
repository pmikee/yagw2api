package de.justi.yagw2api.core.wrapper.model.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

import de.justi.yagw2api.core.wrapper.model.IGuild;
import de.justi.yagw2api.core.wrapper.model.IUnmodifiable;


class Guild implements IGuild, IUnmodifiable{
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
	

	@Override
	public int hashCode() {		
		return Objects.hashCode(this.getClass().getName(),this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof IGuild)) {
			return false;
		}else{
			final IGuild guild = (IGuild)obj;
			return Objects.equal(this.id, guild.getId());
		}
	}
}