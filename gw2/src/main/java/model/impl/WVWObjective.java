package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Locale;

import model.IWVWLocationType;
import model.IWVWObjective;
import model.IWVWObjectiveType;


import com.google.common.base.Objects;

class WVWObjective extends AbstractHasChannel implements IWVWObjective{
	private final IWVWObjectiveType type;
	private final IWVWLocationType location;

	public WVWObjective(IWVWObjectiveType type, IWVWLocationType location){
		checkNotNull(type);
		this.type = type;
		checkNotNull(location);
		this.location = location;
	}
	
	public String getName(Locale locale) {
		return null;
	}

	public IWVWObjectiveType getType() {
		return type;
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("type", this.type).add("name", this.getName(Locale.getDefault())).add("location",this.location).toString();
	}

	public IWVWLocationType getLocation() {
		return this.location;
	}
}
