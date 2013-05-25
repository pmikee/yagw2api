package api.dto.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;

import api.dto.IWVWMapDTO;
import api.dto.IWVWMatchDTO;
import api.dto.IWVWMatchDetailsDTO;
import api.dto.IWVWScoresDTO;
import api.service.IWVWService;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

class WVWMatchDetailsDTO extends AbstractDTOWithService implements IWVWMatchDetailsDTO {

	@Since(1.0)
	@SerializedName("match_id")
	private String id;
	
	@Since(1.0)
	@SerializedName("scores")
	private int[] scores;

	@Since(1.0)
	@SerializedName("maps")
	private WVWMapDTO[] maps;

	public WVWMatchDetailsDTO(IWVWService service) {
		super(service);
	}
	
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
		return Objects.toStringHelper(this).add("id", this.id).add("scores", this.getScores()).add("maps", Arrays.deepToString(this.maps)).add("match", this.getMatch()).toString();
	}

	public Optional<IWVWMatchDTO> getMatch() {
		checkState(this.getMatchID() != null);
		return this.getService().retrieveMatch(this.getMatchID());
	}
}