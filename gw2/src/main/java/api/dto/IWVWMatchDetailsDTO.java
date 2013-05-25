package api.dto;

import com.google.common.base.Optional;

public interface IWVWMatchDetailsDTO {
	String getMatchID();
	IWVWScoresDTO getScores();
	IWVWMapDTO[] getMaps();
	Optional<IWVWMatchDTO> getMatch();
}
