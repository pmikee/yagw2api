package de.justi.yagw2api.arenanet;

public interface IWVWMapDTO {
	int getRedScore();
	int getGreenScore();
	int getBlueScore();
	IWVWObjectiveDTO[] getObjectives();
	String getType();
}
