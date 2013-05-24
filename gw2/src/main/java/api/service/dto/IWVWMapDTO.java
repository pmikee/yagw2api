package api.service.dto;

public interface IWVWMapDTO {
	IWVWScoresDTO getScores();
	IWVWObjectiveDTO[] getObjectives();
	String getType();
}
