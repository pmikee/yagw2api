package de.justi.yagw2api.analyzer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


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
