package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStatusDTO;

final class ResultWrapper {
	@SerializedName("api")
	@Since(1.0)
	private APIStatusDTO api;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("api", this.api).toString();
	}

	public IAPIStatusDTO getAPI() {
		return this.api;
	}
}
