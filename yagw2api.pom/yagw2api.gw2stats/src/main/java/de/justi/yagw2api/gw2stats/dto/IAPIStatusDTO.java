package de.justi.yagw2api.gw2stats.dto;

public interface IAPIStatusDTO {

	public abstract IStatusDTO getTilesStatus();

	public abstract IStatusDTO getObjectiveNamesStatus();

	public abstract IStatusDTO getMatchesStatus();

	public abstract IStatusDTO getMatchDetailsStatus();

	public abstract IStatusDTO getWorldNamesStatus();

	public abstract IStatusDTO getRecipesStatus();

	public abstract IStatusDTO getRecipeDetailsStatus();

	public abstract IStatusDTO getMapsStatus();

	public abstract IStatusDTO getMapNamesStatus();

	public abstract IStatusDTO getMapFloorStatus();

	public abstract IStatusDTO getItemsStatus();

	public abstract IStatusDTO getGuildDetailsStatus();

	public abstract IStatusDTO getEventsStatus();

	public abstract IStatusDTO getEventNamesStatus();

	public abstract IStatusDTO getEventsDetailsStatus();

	public abstract IStatusDTO getContinentsStatus();

	public abstract IStatusDTO getColorsStatus();

	public abstract IStatusDTO getBuildStatus();

	public abstract IStatusDTO getStatusStatus();

	public abstract IStatusDTO getStatusCodesStatus();

}
