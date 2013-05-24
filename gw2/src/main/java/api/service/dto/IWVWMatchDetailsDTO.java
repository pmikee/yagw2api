package api.service.dto;

public interface IWVWMatchDetailsDTO {
	String getMatchID();
	IWVWScoresDTO getScores();
	IWVWMapDTO[] getMaps();
}
