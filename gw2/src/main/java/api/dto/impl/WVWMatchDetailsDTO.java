package api.dto.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import utils.InjectionHelper;

import api.dto.IWVWMapDTO;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.dto.IWVWScoresDTO;
import api.service.IWVWService;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWMatchDetailsDTO implements IWVWMatchDetailsDTO {
	private static final transient IWVWService SERVICE = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWService.class);
	@Since(1.0)
	@SerializedName("match_id")
	private String id;

	@Since(1.0)
	@SerializedName("scores")
	private int[] scores;

	@Since(1.0)
	@SerializedName("maps")
	private WVWMapDTO[] maps;

	public String getMatchID() {
		return this.id;
	}

	public IWVWScoresDTO getScores() {
		return WVWScoresDTO.fromArray(this.scores);
	}

	public IWVWMapDTO[] getMaps() {
		return this.maps;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("scores", this.getScores()).add("maps", Arrays.deepToString(this.maps))
				.add("match", this.getMatch()).toString();
	}

	public Optional<IWVWMatchDTO> getMatch() {
		checkState(this.getMatchID() != null);
		return SERVICE.retrieveMatch(this.getMatchID());
	}

	@Override
	public Optional<IWVWMapDTO> getMapForTypeString(String dtoMapTypeString) {
		checkNotNull(dtoMapTypeString);
		IWVWMapDTO map = null;
		int index = 0;
		while (map == null && index < this.maps.length) {
			map = this.maps[index].getType().equalsIgnoreCase(dtoMapTypeString) ? this.maps[index] : map;
			index++;
		}
		return Optional.fromNullable(map);
	}

	@Override
	public IWVWMapDTO getCenterMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(CENTER_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	@Override
	public IWVWMapDTO getRedMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(RED_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	@Override
	public IWVWMapDTO getGreenMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(GREEN_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	@Override
	public IWVWMapDTO getBlueMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(BLUE_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}
}
