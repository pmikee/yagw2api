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
import model.wvw.IWVWObjective;

public class WVWMapBuilder implements IWVWMapBuilder {
	private IWVWMapType type = WVWMapType.CENTER;
	private Map<IWVWLocationType, IHasWVWLocation> contentMappedByLocation = new HashMap<IWVWLocationType, IHasWVWLocation>();

	public WVWMapBuilder() {
	}

	@Override
	public IWVWMap build() {
		checkState(WVWLocationType.forMapTyp(this.type).isPresent());
		for (IWVWLocationType location : WVWLocationType.forMapTyp(this.type).get()) {
			if (!this.contentMappedByLocation.containsKey(location)) {
				if (location.isObjectiveLocation()) {
					this.contentMappedByLocation.put(location, new WVWObjective(location));
				} else {
					// TODO
				}
			}
		}
		return new WVWMap(this.type, this.contentMappedByLocation.values());
	}

	@Override
	public IWVWMapBuilder type(IWVWMapType type) {
		this.type = checkNotNull(type);
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
