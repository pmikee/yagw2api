package api.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;

import api.dto.IWVWMatchDTO;
import api.dto.IWorldNameDTO;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWMatchDTO extends AbstractDTOWithService implements IWVWMatchDTO {


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
		return this.getService().retrieveWorldName(locale, this.getRedWorldId());
	}

	public Optional<IWorldNameDTO> getGreenWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getGreenWorldId() > 0);
		return this.getService().retrieveWorldName(locale, this.getGreenWorldId());
	}

	public Optional<IWorldNameDTO> getBlueWorldName(Locale locale) {
		checkNotNull(locale);
		checkState(this.getBlueWorldId() > 0);
		return this.getService().retrieveWorldName(locale, this.getBlueWorldId());
	}
}
