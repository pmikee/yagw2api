package de.justi.yagw2api.arenanet.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.DTOConstants;
import de.justi.yagw2api.arenanet.IWVWMapDTO;
import de.justi.yagw2api.arenanet.IWVWMatchDTO;
import de.justi.yagw2api.arenanet.IWVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;

final class WVWMatchDetailsDTO implements IWVWMatchDetailsDTO {

	@Since(1.0)
	@SerializedName("match_id")
	private String id;

	@Since(1.0)
	@SerializedName("scores")
	private int[] scores;

	@Since(1.0)
	@SerializedName("maps")
	private WVWMapDTO[] maps;

	@Override
	public String getMatchID() {
		return this.id;
	}

	@Override
	public int getRedScore() {
		return this.scores[0];
	}

	@Override
	public int getGreenScore() {
		return this.scores[2];
	}

	@Override
	public int getBlueScore() {
		return this.scores[1];
	}

	@Override
	public IWVWMapDTO[] getMaps() {
		return this.maps;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", this.id).add("redScore", this.getRedScore()).add("greenScore", this.getGreenScore()).add("blueScore", this.getBlueScore())
				.add("maps", Arrays.deepToString(this.maps)).toString();
	}

	@Override
	public Optional<IWVWMatchDTO> getMatch() {
		checkState(this.getMatchID() != null);
		return YAGW2APIArenanet.getInstance().getWVWService().retrieveMatch(this.getMatchID());
	}

	@Override
	public Optional<IWVWMapDTO> getMapForTypeString(String dtoMapTypeString) {
		checkNotNull(dtoMapTypeString);
		IWVWMapDTO map = null;
		int index = 0;
		while ((map == null) && (index < this.maps.length)) {
			map = this.maps[index].getType().equalsIgnoreCase(dtoMapTypeString) ? this.maps[index] : map;
			index++;
		}
		return Optional.fromNullable(map);
	}

	@Override
	public IWVWMapDTO getCenterMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(DTOConstants.CENTER_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	@Override
	public IWVWMapDTO getRedMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(DTOConstants.RED_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	@Override
	public IWVWMapDTO getGreenMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(DTOConstants.GREEN_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	@Override
	public IWVWMapDTO getBlueMap() {
		final Optional<IWVWMapDTO> map = this.getMapForTypeString(DTOConstants.BLUE_MAP_TYPE_STRING);
		checkState(map.isPresent());
		return map.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		result = (prime * result) + Arrays.hashCode(maps);
		result = (prime * result) + Arrays.hashCode(scores);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WVWMatchDetailsDTO other = (WVWMatchDetailsDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (!Arrays.equals(maps, other.maps)) {
			return false;
		}
		if (!Arrays.equals(scores, other.scores)) {
			return false;
		}
		return true;
	}

}