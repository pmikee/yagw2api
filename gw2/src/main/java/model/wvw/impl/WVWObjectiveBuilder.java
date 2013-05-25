package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import model.IModelFactory;
import model.IWorld;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import model.wvw.IWVWObjectiveBuilder;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Optional;
import com.google.inject.Inject;

class WVWObjectiveBuilder implements IWVWObjectiveBuilder {
	private Optional<IWVWLocationType> location = Optional.absent();
	private final IWVWModelFactory wvwModelFactory;
	@SuppressWarnings("unused")
	private final IModelFactory modelFactory;
	private Optional<IWorld> owner = Optional.absent();
	
	@Inject
	public WVWObjectiveBuilder(IWVWModelFactory wvwModelFactory, IModelFactory modelFactory) {
		this.wvwModelFactory = checkNotNull(wvwModelFactory);
		this.modelFactory = checkNotNull(modelFactory);
	}
	
	@Override
	public IWVWObjective build() {
		checkState(this.location.isPresent());
		final WVWObjective result = new WVWObjective(this.location.get());
		if(this.owner.isPresent()) {
			result.capture(this.owner.get());
		}
		return result;
	}

	@Override
	public IWVWObjectiveBuilder fromDTO(IWVWObjectiveDTO dto) {
		checkNotNull(dto);		
		return this.location(this.wvwModelFactory.getLocationTypeForObjectiveId(dto.getId()).get());
	}

	@Override
	public IWVWObjectiveBuilder location(IWVWLocationType location) {
		this.location = Optional.of(location);
		return this;
	}

	@Override
	public IWVWObjectiveBuilder owner(IWorld world) {
		this.owner = Optional.fromNullable(world);
		return this;
	}

}
