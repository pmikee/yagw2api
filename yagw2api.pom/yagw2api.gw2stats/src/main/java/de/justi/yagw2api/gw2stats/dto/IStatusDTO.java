package de.justi.yagw2api.gw2stats.dto;

public interface IStatusDTO {

	public abstract String getTime();

	public abstract int getRecords();

	public abstract int getRetrieve();

	public abstract int getPing();

	public abstract String getStatus();

}
