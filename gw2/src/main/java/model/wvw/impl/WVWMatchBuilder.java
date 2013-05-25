package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;

import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWMap;
import model.wvw.IWVWMatch;
import model.wvw.IWVWMatchBuilder;
import model.wvw.IWVWModelFactory;
import utils.InjectionHelper;
import api.dto.IWVWMatchDTO;

import com.google.common.base.Optional;

public class WVWMatchBuilder implements IWVWMatchBuilder {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	private static final IModelFactory MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IModelFactory.class);
	
	private Optional<String> id = Optional.absent();
	private Optional<IWVWMap> centerMap = Optional.absent();
	private Optional<IWVWMap> redMap =  Optional.absent();
	private Optional<IWVWMap> greenMap =  Optional.absent();
	private Optional<IWVWMap> blueMap =  Optional.absent();
	private Optional<IWorld> redWorld = Optional.absent();
	private Optional<IWorld> greenWorld = Optional.absent();
	private Optional<IWorld> blueWorld = Optional.absent();
	
	@Override
	public IWVWMatch build() {
		return WVW_MODEL_FACTORY.createWVWMatch(this.id.get(), this.redWorld.get(), this.greenWorld.get(), this.blueWorld.get(), this.centerMap.get(), this.redMap.get(), this.greenMap.get(), this.blueMap.get());
	}

	@Override
	public IWVWMatchBuilder fromMatchDTO(IWVWMatchDTO dto, Locale locale) {
		checkNotNull(locale);
		this.id = Optional.of(dto.getId());
		this.centerMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getCenterMap()).build());
		this.redMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getRedMap()).build());
		this.greenMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getGreenMap()).build());
		this.blueMap = Optional.of(WVW_MODEL_FACTORY.createMapBuilder().fromDTO(dto.getDetails().get().getBlueMap()).build());
		this.redWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getRedWorldId(), dto.getRedWorldName(locale).get().getName()));
		this.greenWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getGreenWorldId(), dto.getGreenWorldName(locale).get().getName()));
		this.blueWorld = Optional.of(MODEL_FACTORY.createWorld(dto.getBlueWorldId(), dto.getGreenWorldName(locale).get().getName()));
		return this;
	}

}
