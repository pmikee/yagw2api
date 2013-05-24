package api.service.dto.impl;

import api.service.dto.IWVWMatchDTO;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWMatchDTO implements IWVWMatchDTO{
	@Since(1.0)
	@SerializedName("wvw_match_id")
	private String id;
	@Since(1.0)
	@SerializedName("red_world_id")
	private int redWorldId;
	@Since(1.0)
	@SerializedName("blue_world_id")
	private int blueWorldId;
	@Since(1.0)
	@SerializedName("green_world_id")
	private int greenWorldId;
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("redWorldId", this.redWorldId).add("blueWorldId", this.blueWorldId).add("greenWorldId", this.greenWorldId).toString();
	}

	public int getRedWorldId() {
		return this.redWorldId;
	}

	public int getGreenWorldId() {
		return this.greenWorldId;
	}

	public int getBlueWorldId() {
		return this.blueWorldId;
	}

	public String getId() {
		return this.id;
	}	
}
