package model.wvw.impl;

import java.util.Set;

import model.wvw.IWVWLocationType;
import model.wvw.IWVWMap;
import model.wvw.IWVWMapType;
import model.wvw.IWVWMatch;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import model.wvw.IWVWScores;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMap.IWVWMapBuilder createMapBuilder() {
		return new WVWMap.WVWMapBuilder();
	}

	@Override
	public IWVWObjective.IWVWObjectiveBuilder createObjectiveBuilder() {
		return new WVWObjective.WVWObjectiveBuilder();
	}

	@Override
	public Set<IWVWMapType> allMapTypes() {
		return ImmutableSet.<IWVWMapType> copyOf(WVWMapType.values());
	}

	@Override
	public IWVWMapType getCenterMapType() {
		return WVWMapType.CENTER;
	}

	@Override
	public IWVWMapType getGreenMapType() {
		return WVWMapType.GREEN;
	}

	@Override
	public IWVWMapType getRedMapType() {
		return WVWMapType.RED;
	}

	@Override
	public IWVWMapType getBlueMapType() {
		return WVWMapType.BLUE;
	}

	@Override
	public IWVWMapType getMapTypeForDTOString(String string) {
		return WVWMapType.fromDTOString(string);
	}

	@Override
	public Set<IWVWLocationType> allLocationTypes() {
		return ImmutableSet.<IWVWLocationType> copyOf(WVWLocationType.values());
	}

	@Override
	public Optional<IWVWLocationType> getLocationTypeForObjectiveId(int objectiveId) {
		return WVWLocationType.forObjectiveId(objectiveId);
	}

	@Override
	public IWVWScores createScores() {
		return new WVWScores();
	}

	@Override
	public IWVWMatch.IWVWMatchBuilder createMatchBuilder() {
		return new WVWMatch.WVWMatchBuilder();
	}
}