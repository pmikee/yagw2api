package de.justi.yagw2api.mumblelink.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.justi.yagw2api.mumblelink.IMumbleLinkAvatar;

public final class MumbleLinkAvatar implements IMumbleLinkAvatar {
	// STATICS
	private static final Logger LOGGER = LoggerFactory.getLogger(MumbleLinkAvatar.class);
	private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	public static IMumbleLinkAvatar of(String json) {
		checkNotNull(json, "missing json");
		return ofWithRetry(json, json);
	}
	private static  IMumbleLinkAvatar ofWithRetry(String json, String originalJson) {
		checkNotNull(originalJson, "missing originalJson");
		checkNotNull(json, "missing json");
		try {
			return GSON.fromJson(json, MumbleLinkAvatar.class);
		} catch (JsonSyntaxException e) {
			LOGGER.trace("Invalid json: {}", json);
			
			final int end = json.indexOf('}') + 1;
			if (end > 0 && end < json.length()) {				
				return ofWithRetry(json.substring(0, end), originalJson);
			} else {
				throw new JsonSyntaxException("Invalid json: "+originalJson);
			}
		}
	}

	// FIELDS
	@Expose
	private final String name;
	@Expose
	private final boolean commander;
	@Expose
	@SerializedName("team_color_id")
	private final int teamColorId;
	@Expose
	private final int profession;
	@Expose
	@SerializedName("map_id")
	private final long mapId;
	@Expose
	@SerializedName("world_id")
	private final long worldId;

	// CONSTRUCTOR

	private MumbleLinkAvatar() {
		this.name = null;
		this.commander = false;
		this.teamColorId = -1;
		this.profession = -1;
		this.mapId = -1L;
		this.worldId = -1L;
	}

	// METHODS
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isCommander() {
		return this.commander;
	}

	@Override
	public int getTeamColorId() {
		return teamColorId;
	}

	@Override
	public int getProfession() {
		return profession;
	}

	@Override
	public long getMapId() {
		return mapId;
	}

	@Override
	public long getWorldId() {
		return worldId;
	}

	public String toString() {
		return MoreObjects.toStringHelper(this).add("name", name).add("isCommander", this.commander).add("profession", this.profession).add("teamColorId", this.teamColorId)
				.add("mapId", this.mapId).add("wolrdId", this.worldId).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (commander ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MumbleLinkAvatar other = (MumbleLinkAvatar) obj;
		if (commander != other.commander)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
