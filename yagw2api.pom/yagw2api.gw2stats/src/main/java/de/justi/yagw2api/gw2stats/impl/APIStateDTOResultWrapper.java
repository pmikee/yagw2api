package de.justi.yagw2api.gw2stats.impl;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.IAPIStateDTO;

final class APIStateDTOResultWrapper {
	@SerializedName("api")
	@Since(1.0)
	private Map<String, APIStateDTO> result = Collections.emptyMap();

	public Map<String, ? extends IAPIStateDTO> getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(this.result).toString();
	}
}
