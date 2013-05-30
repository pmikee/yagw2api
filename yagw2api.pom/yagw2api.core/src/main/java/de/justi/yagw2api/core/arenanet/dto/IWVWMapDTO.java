package de.justi.yagw2api.core.arenanet.dto;

public interface IWVWMapDTO {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	IWVWObjectiveDTO[] getObjectives();
	String getType();
}
