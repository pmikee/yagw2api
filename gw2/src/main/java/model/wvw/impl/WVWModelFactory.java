package model.wvw.impl;

import java.util.Set;

import utils.InjectionHelper;

import model.wvw.IWVWMapBuilder;
import model.wvw.IWVWMapType;
import model.wvw.IWVWModelFactory;

import com.google.common.collect.ImmutableSet;

public class WVWModelFactory implements IWVWModelFactory {

	@Override
	public IWVWMapBuilder createMapBuilder() {
		return InjectionHelper.INSTANCE.getInjector().getInstance(IWVWMapBuilder.class);
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
}