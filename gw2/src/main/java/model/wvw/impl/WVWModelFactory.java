package model.wvw.impl;

import java.util.Set;

import model.wvw.IWVWLocationType;
import model.wvw.IWVWMapBuilder;
import model.wvw.IWVWMapType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjectiveBuilder;
import utils.InjectionHelper;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMapBuilder createMapBuilder() {
		return InjectionHelper.INSTANCE.getInjector().getInstance(IWVWMapBuilder.class);
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
	public IWVWMapType createMapTypeFromDTOString(String string) {
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
}