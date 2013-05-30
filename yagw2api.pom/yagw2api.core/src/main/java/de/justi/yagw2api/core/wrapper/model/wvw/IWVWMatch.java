package de.justi.yagw2api.core.wrapper.model.wvw;

import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.arenanet.dto.IWVWMatchDTO;
import de.justi.yagw2api.core.wrapper.model.IHasChannel;
import de.justi.yagw2api.core.wrapper.model.IWorld;


public interface IWVWMatch extends IHasChannel {
	interface IWVWMatchBuilder {
		IWVWMatch build();
		IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale);
		IWVWMatchBuilder redScore(int score);
		IWVWMatchBuilder blueScore(int score);
		IWVWMatchBuilder greenScore(int score);
	}
	
	Set<IWorld> searchWorldsByNamePattern(Pattern searchPattern);
	String getId();
	IWorld[] getWorlds();
	IWorld getRedWorld();
	IWorld getGreenWorld();
	IWorld getBlueWorld(); 
	Optional<IWorld> getWorldByDTOOwnerString(String dtoOwnerString);
	IWVWMap getCenterMap();
	IWVWMap getBlueMap();
	IWVWMap getRedMap();
	IWVWMap getGreenMap();
	IWVWScores getScores();
	
	IWVWMatch createUnmodifiableReference();
}
