package de.justi.yagw2api.core.arenanet.dto;

public interface IGuildEmblemDTO {
	int getBackgroundId();
	int getForegroundId();
	int getBackgroundColorId();
	int getForegroundPrimaryColorId();
	int getForegroundSecondaryColorId();
	int[] getFlags();	
}
