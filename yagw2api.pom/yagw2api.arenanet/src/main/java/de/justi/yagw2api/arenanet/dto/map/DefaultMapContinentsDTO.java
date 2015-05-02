package de.justi.yagw2api.arenanet.dto.map;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class DefaultMapContinentsDTO implements MapContinentsDTO {

	@Since(1.0)
	@SerializedName("continents")
	private Map<String, DefaultMapContinentDTO> continents;

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("continents", this.continents).toString();
	}

	@Override
	public Collection<MapContinentDTO> getContinents() {
		return Collections.unmodifiableCollection(this.continents.values());
	}

}
