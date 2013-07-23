package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDescriptionDTO;

final class APIStateDescriptionDTO implements IAPIStateDescriptionDTO {
	@SerializedName("description")
	@Since(1.0)
	private String description;

	@Override
	public String toString() {
		return Objects.toStringHelper(this).addValue(this.description).toString();
	}

	/**
	 * @return the description
	 */
	@Override
	public final String getDescription() {
		return description;
	}
}
