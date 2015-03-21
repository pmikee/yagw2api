package de.justi.yagw2api.arenanet.dto.map;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class DefaultMapsDTO implements MapsDTO {

	@SerializedName("maps")
	@Since(1.0)
	private final Map<String, DefaultMapsMapDTO> maps = ImmutableMap.of();

	@Override
	public Map<String, MapsMapDTO> getMaps() {
		return Collections.unmodifiableMap(this.maps);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("maps", this.maps).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.maps == null) ? 0 : this.maps.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DefaultMapsDTO))
			return false;
		DefaultMapsDTO other = (DefaultMapsDTO) obj;
		if (this.maps == null) {
			if (other.maps != null)
				return false;
		} else if (!this.maps.equals(other.maps))
			return false;
		return true;
	}

}
