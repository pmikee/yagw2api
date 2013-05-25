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
import utils.InjectionHelper;
import api.dto.IWVWMapDTO;
import api.dto.IWVWObjectiveDTO;

import com.google.common.base.Optional;

public class WVWMapBuilder implements IWVWMapBuilder {
	private static final IWVWModelFactory WVW_MODEL_FACTORY = InjectionHelper.INSTANCE.getInjector().getInstance(IWVWModelFactory.class);
	
	private Map<IWVWLocationType, IHasWVWLocation> contentMappedByLocation = new HashMap<IWVWLocationType, IHasWVWLocation>();
	private Optional<IWVWMapType> type = Optional.absent();
	
	public WVWMapBuilder() {
	}

	@Override
	public IWVWMap build() {
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

	@Override
	public IWVWMapBuilder fromDTO(IWVWMapDTO dto) {
		checkNotNull(dto);
		for (IWVWObjectiveDTO objectiveDTO : dto.getObjectives()) {
			this.objective(WVW_MODEL_FACTORY.createObjectiveBuilder().fromDTO(objectiveDTO).build());
		}
		this.type(WVW_MODEL_FACTORY.getMapTypeForDTOString(dto.getType()));
		return this;
	}

}
