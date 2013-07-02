package de.justi.yagw2api.wrapper.model.types;

public enum WorldLocationType implements IWorldLocationType{
	NORTH_AMERICA,
	EUROPE;

	@Override
	public boolean isNorthAmerica() {
		return this.equals(NORTH_AMERICA);
	}

	@Override
	public boolean isEurope() {
		return this.equals(EUROPE);
	}
}
