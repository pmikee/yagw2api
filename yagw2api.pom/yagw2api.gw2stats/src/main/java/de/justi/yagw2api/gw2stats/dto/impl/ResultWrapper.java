package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStatesDTO;

final class ResultWrapper {
	@SerializedName("api")
	@Since(1.0)
	private APIStatesDTO apiStates;

	@SerializedName("status_codes")
	@Since(1.0)
	private APIStateDescriptionsDTO descriptions;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("api", this.apiStates).add("descriptions", this.descriptions).toString();
	}

	public IAPIStatesDTO getAPIStates() {
		return this.apiStates;
	}

	public APIStateDescriptionsDTO getAPIStateDescriptions() {
		return this.descriptions;
	}
}
