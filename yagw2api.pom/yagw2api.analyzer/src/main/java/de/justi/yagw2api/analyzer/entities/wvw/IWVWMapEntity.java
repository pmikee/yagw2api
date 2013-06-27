package de.justi.yagw2api.analyzer.entities.wvw;

import java.util.Date;
import java.util.Map;

import com.google.common.base.Optional;

import de.justi.yagw2api.analyzer.entities.IEntity;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;

public interface IWVWMapEntity extends IEntity {
	IWVWMatchEntity getMatch();

	Map<Date, IWVWScoresEmbeddable> getScores();

	Optional<IWVWScoresEmbeddable> getLatestScores();

	void synchronizeWithModel(Date timestamp, IWVWMap model, boolean setupMatchReference);
}
