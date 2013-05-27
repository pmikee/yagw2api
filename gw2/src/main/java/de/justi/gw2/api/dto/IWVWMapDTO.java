package de.justi.gw2.api.dto;

public interface IWVWMapDTO {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	IWVWObjectiveDTO[] getObjectives();
	String getType();
}
