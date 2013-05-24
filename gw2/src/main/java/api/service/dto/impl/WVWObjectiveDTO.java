package api.service.dto.impl;

import api.service.dto.IWVWObjectiveDTO;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWObjectiveDTO implements IWVWObjectiveDTO {
	@Since(1.0)
	@SerializedName("id")
	private int id;
	@Since(1.0)
	@SerializedName("owner")
	private String owner;
	@Since(1.0)
	@SerializedName("owner_guild")
	private String guildId;
	
	public int getId() {
		return this.id;
	}
	public String getOwner() {
		return this.owner;
	}
	public String getGuildId() {
		return this.guildId;
	}
	
	public String toString(){
		return Objects.toStringHelper(this).add("id", this.id).add("owner", this.owner).add("guildId", this.guildId).toString();
	}
}
