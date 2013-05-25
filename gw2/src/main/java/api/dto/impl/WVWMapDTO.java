package api.dto.impl;

import java.util.Arrays;

import api.dto.IWVWMapDTO;
import api.dto.IWVWObjectiveDTO;

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


	

	public IWVWObjectiveDTO[] getObjectives() {
		return this.objectives;
	}

	public String getType() {
		return this.type;
	}

	public String toString() {
		return Objects.toStringHelper(this).add("redScore", this.getRedScore()).add("greenScore", this.getGreenScore()).add("blueScore", this.getBlueScore()).add("objectives", Arrays.deepToString(this.objectives)).add("type", this.type)
				.toString();
	}

	@Override
	public int getRedScore() {
		return this.scores[0];
	}

	@Override
	public int getGreenScore() {
		return this.scores[1];
	}

	@Override
	public int getBlueScore() {
		return this.scores[2];
	}
}
