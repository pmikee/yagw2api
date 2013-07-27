package de.justi.yagw2api.analyzer;

import java.util.Date;
import java.util.Map;

import com.google.common.base.Optional;

import de.justi.yagw2api.wrapper.IWVWMapType;

public interface IWVWMapEntity extends IEntity {
	void setMatch(IWVWMatchEntity match);

	IWVWMatchEntity getMatch();

	void addScores(Date timestamp, IWVWScoresEmbeddable scores);

	Map<Date, IWVWScoresEmbeddable> getScores();

	Optional<IWVWScoresEmbeddable> getLatestScores();

	IWVWMapType getType();

	void setType(IWVWMapType type);
}
