package model.wvw.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.HashMap;
import java.util.Map;

import model.wvw.IHasWVWLocation;
import model.wvw.IWVWLocationType;
import model.wvw.IWVWMap;
import model.wvw.IWVWMapBuilder;
import model.wvw.IWVWMapType;
import model.wvw.IWVWModelFactory;
import model.wvw.IWVWObjective;

import com.google.common.base.Optional;
import com.google.inject.Inject;

public class WVWMapBuilder implements IWVWMapBuilder {
	private Map<IWVWLocationType, IHasWVWLocation> contentMappedByLocation = new HashMap<IWVWLocationType, IHasWVWLocation>();
	private Optional<IWVWMapType> type = Optional.absent();
	private final IWVWModelFactory wvwModelFactory;
	
	@Inject
	public WVWMapBuilder(IWVWModelFactory wvwModelFactory) {
		this.wvwModelFactory = checkNotNull(wvwModelFactory);
	}

	@Override
	public IWVWMap build() {
		if(!this.type.isPresent()) {
			this.type = Optional.of(this.wvwModelFactory.getCenterMapType());
		}
		checkState(this.type.isPresent());
		checkState(WVWLocationType.forMapTyp(this.type.get()).isPresent());
		for (IWVWLocationType location : WVWLocationType.forMapTyp(this.type.get()).get()) {
			if (!this.contentMappedByLocation.containsKey(location)) {
				if (location.isObjectiveLocation()) {
					this.contentMappedByLocation.put(location, new WVWObjective(location));
				} else {
					// TODO
				}
			}
		}
		return new WVWMap(this.type.get(), this.contentMappedByLocation.values());
	}

	@Override
	public IWVWMapBuilder type(IWVWMapType type) {
		this.type = Optional.fromNullable(type);
		return this;
	}

	@Override
	public IWVWMapBuilder objective(IWVWObjective objective) {
		checkNotNull(objective);
		checkNotNull(objective.getLocation());
		checkState(!this.contentMappedByLocation.containsKey(objective.getLocation()));
		this.contentMappedByLocation.put(objective.getLocation(), objective);
		return this;
	}

}
