package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import model.wvw.IWVWObjectiveBuilder;
import utils.InjectionHelper;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Optional;

class WVWObjectiveBuilder implements IWVWObjectiveBuilder {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	@SuppressWarnings("unused")
	private static final IModelFactory MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IModelFactory.class);
	
	private Optional<IWVWLocationType> location = Optional.absent();
	private Optional<IWorld> owner = Optional.absent();
	
	@Override
	public IWVWObjective build() {
		final IWVWObjective result = WVW_MODEL_FACTORY.createObjective(this.location.get());
		if(this.owner.isPresent()) {
			result.capture(this.owner.get());
		}
		return result;
	}

	@Override
	public IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto) {
		checkNotNull(dto);		
		return this.location(WVW_MODEL_FACTORY.getLocationTypeForObjectiveId(dto.getId()).get());
	}

	@Override
	public IWVWObjectiveBuilder location(IWVWLocationType location) {
		this.location = Optional.fromNullable(location);
		return this;
	}

	@Override
	public IWVWObjectiveBuilder owner(IWorld world) {
		this.owner = Optional.fromNullable(world);
		return this;
	}

}
