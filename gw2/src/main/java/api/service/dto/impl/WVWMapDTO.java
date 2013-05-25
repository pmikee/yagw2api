package api.service.dto.impl;

import java.util.Arrays;

import api.service.dto.IWVWMapDTO;
import api.service.dto.IWVWObjectiveDTO;
import api.service.dto.IWVWScoresDTO;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWMapDTO implements IWVWMapDTO {

	@Since(1.0)
	@SerializedName("scores")
	private int[] scores;
	@Since(1.0)
	@SerializedName("objectives")
	private WVWObjectiveDTO[] objectives;
	@Since(1.0)
	@SerializedName("type")
	private String type;


	public IWVWScoresDTO getScores() {
		return WVWScoresDTO.fromArray(this.scores);
	}

	public IWVWObjectiveDTO[] getObjectives() {
		return this.objectives;
	}

	public String getType() {
		return this.type;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("scores", this.getScores()).add("objectives", Arrays.deepToString(this.objectives)).add("type", this.type)
				.toString();
	}
}
