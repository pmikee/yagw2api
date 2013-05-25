package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import api.dto.IWVWObjectiveDTO;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;
import model.wvw.IWVWObjectiveBuilder;

class WVWObjectiveBuilder implements IWVWObjectiveBuilder {
	private Optional<IWVWLocationType> location = Optional.absent();
	private final IWVWModelFactory wvwModelFactory;
	
	@Inject
	public WVWObjectiveBuilder(IWVWModelFactory wvwModelFactory) {
		this.wvwModelFactory = checkNotNull(wvwModelFactory);
	}
	
	@Override
	public IWVWObjective build() {
		checkState(this.location.isPresent());
		final WVWObjective result = new WVWObjective(this.location.get());
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

}
