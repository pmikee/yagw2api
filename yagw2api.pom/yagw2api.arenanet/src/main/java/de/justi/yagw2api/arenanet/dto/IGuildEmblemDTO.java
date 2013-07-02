package de.justi.yagw2api.arenanet.dto;

public interface IGuildEmblemDTO {
	int getBackgroundId();
	int getForegroundId();
	int getBackgroundColorId();
	int getForegroundPrimaryColorId();
	int getForegroundSecondaryColorId();
	String[] getFlags();	
}
