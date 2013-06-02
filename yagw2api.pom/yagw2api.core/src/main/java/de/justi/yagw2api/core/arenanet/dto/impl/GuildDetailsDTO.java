package de.justi.yagw2api.core.arenanet.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.core.arenanet.dto.IGuildDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IGuildEmblemDTO;

public class GuildDetailsDTO implements IGuildDetailsDTO {
	@SerializedName("guild_id")
	@Since(1.0)
	private String id;
	
	@SerializedName("guild_name")
	@Since(1.0)
	private String name;

	@SerializedName("tag")
	@Since(1.0)
	private String tag;
	
	@SerializedName("emblem")
	@Since(1.0)
	private GuildEmblemDTO emblem;
	
	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getTag() {
		return this.tag;
	}

	@Override
	public IGuildEmblemDTO getEmblem() {
		return this.emblem;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.getId()).add("name", this.getName()).add("tag", this.getTag()).add("emblem", this.getEmblem()).toString();
	}
}
