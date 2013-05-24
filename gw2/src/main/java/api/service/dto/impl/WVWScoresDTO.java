package api.service.dto.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import api.service.dto.IWVWScoresDTO;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWScoresDTO implements IWVWScoresDTO {
	@Since(1.0)
	@SerializedName("0")
	private int red;
	@Since(1.0)
	@SerializedName("1")
	private int green;
	@Since(1.0)
	@SerializedName("2")
	private int blue;
	
	static WVWScoresDTO fromArray(int[] scores){
		checkNotNull(scores);
		checkArgument(scores.length == 3);
		final WVWScoresDTO scoresDTO = new WVWScoresDTO();
		scoresDTO.red = scores[0];
		scoresDTO.green = scores[1];
		scoresDTO.blue = scores[2];
		return scoresDTO;
	}
	
	
	public int getRedScores() {
		return this.red;
	}

	public int getBlueScores() {
		return this.blue;
	}

	public int getGreenScores() {
		return this.green;
	}
	public String toString() {
		return Objects.toStringHelper(this).add("red", this.red).add("green", this.green).add("blue", this.blue).toString();
	}
}
