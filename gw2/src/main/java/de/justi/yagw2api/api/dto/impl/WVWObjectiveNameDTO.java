package de.justi.yagw2api.api.dto.impl;


import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.api.dto.IWVWObjectiveNameDTO;

public class WVWObjectiveNameDTO implements IWVWObjectiveNameDTO {
	@Since(1.0)
	@SerializedName("id")
	private int id;
	@Since(1.0)
	@SerializedName("name")
	private String name;
	

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("name", this.name).toString();
	}

}
