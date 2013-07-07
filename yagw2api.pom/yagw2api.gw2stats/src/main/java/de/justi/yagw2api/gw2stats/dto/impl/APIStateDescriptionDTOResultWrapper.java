package de.justi.yagw2api.gw2stats.dto.impl;

import java.util.Collections;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

final class APIStateDescriptionDTOResultWrapper {
	@SerializedName("status_codes")
	@Since(1.0)
	private Map<String, APIStateDescriptionDTO> result = Collections.emptyMap();

	public Map<String, ? extends APIStateDescriptionDTO> getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(this.result).toString();
	}
}
