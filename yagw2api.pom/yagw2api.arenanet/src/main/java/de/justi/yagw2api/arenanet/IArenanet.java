package de.justi.yagw2api.arenanet;

import java.util.Locale;

public interface IArenanet {

	/**
	 * <p>
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	IWVWService getWVWService();

	/**
	 * <p>
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	IWorldService getWorldService();

	/**
	 * <p>
	 * access to low level api calls returning dtos
	 * <p>
	 * 
	 * @return
	 */
	IGuildService getGuildService();

	Locale getCurrentLocale();

	void setCurrentLocale(Locale locale);

}