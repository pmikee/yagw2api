package de.justi.yagw2api.arenanet.dto.impl;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.arenanet.dto.IWVWMapDTO;
import de.justi.yagw2api.arenanet.dto.IWVWObjectiveDTO;

final class WVWMapDTO implements IWVWMapDTO {

	@Since(1.0)
	@SerializedName("scores")
	private int[] scores;
	@Since(1.0)
	@SerializedName("objectives")
	private WVWObjectiveDTO[] objectives;
	@Since(1.0)
	@SerializedName("type")
	private String type;

	@Override
	public IWVWObjectiveDTO[] getObjectives() {
		return this.objectives;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("redScore", this.getRedScore()).add("greenScore", this.getGreenScore()).add("blueScore", this.getBlueScore())
				.add("objectives", Arrays.deepToString(this.objectives)).add("type", this.type).toString();
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
}
