package de.justi.yagw2api.mumblelink;

public interface IMumbleLinkAvatar {
	String getName();
	
	boolean isCommander();

	long getWorldId();

	long getMapId();

	int getProfession();

	int getTeamColorId();
}
