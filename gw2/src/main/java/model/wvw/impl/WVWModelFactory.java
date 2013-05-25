package model.wvw.impl;

import java.util.Set;

import model.IWorld;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWMap;
import model.wvw.IWVWMapBuilder;
import model.wvw.IWVWMapType;
import model.wvw.IWVWMatch;
import model.wvw.IWVWMatchBuilder;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import model.wvw.IWVWObjectiveBuilder;
import model.wvw.IWVWScores;
import utils.InjectionHelper;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMapBuilder createMapBuilder() {
		return new WVWMapBuilder();
	}

	@Override
	public IWVWObjectiveBuilder createObjectiveBuilder() {
		return InjectionHelper.INSTANCE.getInjector().getInstance(IWVWObjectiveBuilder.class);
	}

	@Override
	public Set<IWVWMapType> allMapTypes() {
		return ImmutableSet.<IWVWMapType>copyOf(WVWMapType.values());
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
		return ImmutableSet.<IWVWLocationType>copyOf(WVWLocationType.values());
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
	public IWVWMatchBuilder createMatchBuilder() {
		return new WVWMatchBuilder();
	}

	@Override
	public IWVWMatch createWVWMatch(String id, IWorld redWorld, IWorld greenWorld, IWorld blueWorld, IWVWMap centerMap, IWVWMap redMap, IWVWMap greenMap,
			IWVWMap blueMap) {
		return new WVWMatch(id, redWorld, greenWorld, blueWorld, centerMap, redMap, greenMap, blueMap);
	}

	@Override
	public IWVWObjective createObjective(IWVWLocationType location) {
		return new WVWObjective(location);
	}
}