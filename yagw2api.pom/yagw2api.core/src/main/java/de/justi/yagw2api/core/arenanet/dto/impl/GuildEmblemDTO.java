package de.justi.yagw2api.core.arenanet.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.core.arenanet.dto.IGuildEmblemDTO;

public class GuildEmblemDTO implements IGuildEmblemDTO {

	@SerializedName("background_id")
	@Since(1.0)
	private int backgroundId;

	@SerializedName("foreground_id")
	@Since(1.0)
	private int foregroundId;

	@SerializedName("flags")
	@Since(1.0)
	private String[] flags;

	@SerializedName("background_color_id")
	@Since(1.0)
	private int backgroundColorId;

	@SerializedName("foreground_primary_color_id")
	@Since(1.0)
	private int foregroundPrimaryColorId;

	@SerializedName("foreground_secondary_color_id")
	@Since(1.0)
	private int foregroundSecondaryColorId;

	@Override
	public int getBackgroundId() {
		return this.backgroundId;
	}

	@Override
	public int getForegroundId() {
		return this.foregroundId;
	}

	@Override
	public int getBackgroundColorId() {
		return this.backgroundColorId;
	}

	@Override
	public int getForegroundPrimaryColorId() {
		return this.foregroundPrimaryColorId;
	}

	@Override
	public int getForegroundSecondaryColorId() {
		return this.foregroundSecondaryColorId;
	}

	@Override
	public String[] getFlags() {
		return this.flags;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("backgroundId", this.getBackgroundId()).add("foregroundId", this.getForegroundId()).add("backgroundColorId", this.getBackgroundColorId())
				.add("foregroundPrimaryColorId", this.getForegroundPrimaryColorId()).add("foregroundSecondaryColorId", this.getForegroundSecondaryColorId()).add("flags", this.flags).toString();
	}
}
