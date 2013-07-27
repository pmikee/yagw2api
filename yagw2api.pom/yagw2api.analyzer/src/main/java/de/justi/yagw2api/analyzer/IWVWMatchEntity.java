package de.justi.yagw2api.analyzer;

import java.util.Date;
import java.util.Map;

import com.google.common.base.Optional;

public interface IWVWMatchEntity extends IEntity {
	void setOriginMatchId(String originMatchId);

	String getOriginMatchId();

	void setStartTimestamp(Date date);

	Date getStartTimestamp();

	void setEndTimestamp(Date date);

	Date getEndTimestamp();

	void setRedMap(IWVWMapEntity map);

	IWVWMapEntity getRedMap();

	void setGreenMap(IWVWMapEntity map);

	IWVWMapEntity getGreenMap();

	void setBlueMap(IWVWMapEntity map);

	IWVWMapEntity getBlueMap();

	void setCenterMap(IWVWMapEntity map);

	IWVWMapEntity getCenterMap();

	void setRedWorld(IWorldEntity world);

	IWorldEntity getRedWorld();

	void setGreenWorld(IWorldEntity world);

	IWorldEntity getGreenWorld();

	void setBlueWorld(IWorldEntity world);

	IWorldEntity getBlueWorld();

	void addScores(Date timestamp, IWVWScoresEmbeddable scores);

	Map<Date, IWVWScoresEmbeddable> getScores();

	Optional<IWVWScoresEmbeddable> getLatestScores();
}
