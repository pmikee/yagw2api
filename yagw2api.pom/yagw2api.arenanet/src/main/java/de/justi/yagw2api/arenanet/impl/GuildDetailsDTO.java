package de.justi.yagw2api.arenanet.impl;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.IGuildDetailsDTO;
import de.justi.yagw2api.arenanet.IGuildEmblemDTO;

final class GuildDetailsDTO implements IGuildDetailsDTO {
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
	public Optional<IGuildEmblemDTO> getEmblem() {
		return Optional.<IGuildEmblemDTO> fromNullable(this.emblem);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.getId()).add("name", this.getName()).add("tag", this.getTag()).add("emblem", this.getEmblem()).toString();
	}
}
