package de.justi.yagw2api.api.dto;

public interface IWVWMapDTO {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	IWVWObjectiveDTO[] getObjectives();
	String getType();
}
