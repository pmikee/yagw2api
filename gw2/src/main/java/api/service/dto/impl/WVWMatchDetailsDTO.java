package api.service.dto.impl;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import api.service.dto.IWVWMapDTO;
import api.service.dto.IWVWMatchDetailsDTO;
import api.service.dto.IWVWScoresDTO;

class WVWMatchDetailsDTO implements IWVWMatchDetailsDTO {
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
		return Objects.toStringHelper(this).add("id", this.id).add("scores", this.getScores()).add("maps", Arrays.deepToString(this.maps)).toString();
	}
}
