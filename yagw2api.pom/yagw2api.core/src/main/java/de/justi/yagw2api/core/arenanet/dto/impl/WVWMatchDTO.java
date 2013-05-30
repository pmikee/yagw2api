package de.justi.yagw2api.core.arenanet.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.core.YAGW2APICore;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDetailsDTO;
import de.justi.yagw2api.core.arenanet.dto.IWorldNameDTO;
import de.justi.yagw2api.core.arenanet.service.IWVWService;

class WVWMatchDTO implements IWVWMatchDTO {
	private static final transient IWVWService SERVICE = YAGW2APICore.getInjector().getInstance(IWVWService.class);

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
		return Objects.toStringHelper(this).add("id", this.id).add("redWorldId", this.redWorldId).add("redWorld", this.getRedWorldName(Locale.getDefault()))
				.add("blueWorldId", this.blueWorldId).add("blueWorld", this.getBlueWorldName(Locale.getDefault())).add("greenWorldId", this.greenWorldId)
				.add("greenWorld", this.getGreenWorldName(Locale.getDefault())).toString();
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

	public Optional<IWorldNameDTO> getRedWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getRedWorldId() > 0);
		return SERVICE.retrieveWorldName(locale, this.getRedWorldId());
	}

	public Optional<IWorldNameDTO> getGreenWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getGreenWorldId() > 0);
		return SERVICE.retrieveWorldName(locale, this.getGreenWorldId());
	}

	public Optional<IWorldNameDTO> getBlueWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getBlueWorldId() > 0);
		return SERVICE.retrieveWorldName(locale, this.getBlueWorldId());
	}

	@Override
	public Optional<IWVWMatchDetailsDTO> getDetails() {
		checkState(this.id != null);
		return Optional.fromNullable(SERVICE.retrieveMatchDetails(this.id));
	}
}
