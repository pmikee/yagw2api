package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStatusDTO;

final class APIStatusDTO implements IAPIStatusDTO {
	@SerializedName("http://gw2stats.net/api/status.json")
	@Since(1.0)
	private StatusDTO statusCodesStatus;

	@SerializedName("http://gw2stats.net/api/status_codes.json")
	@Since(1.0)
	private StatusDTO statusStatus;

	@SerializedName("https://api.guildwars2.com/v1/build.json")
	@Since(1.0)
	private StatusDTO buildStatus;

	@SerializedName("https://api.guildwars2.com/v1/colors.json")
	@Since(1.0)
	private StatusDTO colorsStatus;

	@SerializedName("https://api.guildwars2.com/v1/continents.json")
	@Since(1.0)
	private StatusDTO continentsStatus;

	@SerializedName("https://api.guildwars2.com/v1/event_details.json")
	@Since(1.0)
	private StatusDTO eventsDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/event_names.json")
	@Since(1.0)
	private StatusDTO eventNamesStatus;

	@SerializedName("https://api.guildwars2.com/v1/events.json")
	@Since(1.0)
	private StatusDTO eventsStatus;

	@SerializedName("https://api.guildwars2.com/v1/guild_details.json")
	@Since(1.0)
	private StatusDTO guildDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/items.json")
	@Since(1.0)
	private StatusDTO itemsStatus;

	@SerializedName("https://api.guildwars2.com/v1/map_floor.json")
	@Since(1.0)
	private StatusDTO mapFloorStatus;

	@SerializedName("https://api.guildwars2.com/v1/map_names.json")
	@Since(1.0)
	private StatusDTO mapNamesStatus;

	@SerializedName("https://api.guildwars2.com/v1/maps.json")
	@Since(1.0)
	private StatusDTO mapsStatus;

	@SerializedName("https://api.guildwars2.com/v1/recipe_details.json")
	@Since(1.0)
	private StatusDTO recipeDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/recipes.json")
	@Since(1.0)
	private StatusDTO recipesStatus;

	@SerializedName("https://api.guildwars2.com/v1/world_names.json")
	@Since(1.0)
	private StatusDTO worldNamesStatus;

	@SerializedName("https://api.guildwars2.com/v1/wvw/match_details.json")
	@Since(1.0)
	private StatusDTO matchDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/wvw/matches.json")
	@Since(1.0)
	private StatusDTO matchesStatus;

	@SerializedName("https://api.guildwars2.com/v1/wvw/objective_names.json")
	@Since(1.0)
	private StatusDTO objectiveNamesStatus;

	@SerializedName("https://tiles.guildwars2.com/")
	@Since(1.0)
	private StatusDTO tilesStatus;

	/**
	 * @return the statusCodesStatus
	 */
	@Override
	public final StatusDTO getStatusCodesStatus() {
		return statusCodesStatus;
	}

	/**
	 * @return the statusStatus
	 */
	@Override
	public final StatusDTO getStatusStatus() {
		return statusStatus;
	}

	/**
	 * @return the buildStatus
	 */
	@Override
	public final StatusDTO getBuildStatus() {
		return buildStatus;
	}

	/**
	 * @return the colorsStatus
	 */
	@Override
	public final StatusDTO getColorsStatus() {
		return colorsStatus;
	}

	/**
	 * @return the continentsStatus
	 */
	@Override
	public final StatusDTO getContinentsStatus() {
		return continentsStatus;
	}

	/**
	 * @return the eventsDetailsStatus
	 */
	@Override
	public final StatusDTO getEventsDetailsStatus() {
		return eventsDetailsStatus;
	}

	/**
	 * @return the eventNamesStatus
	 */
	@Override
	public final StatusDTO getEventNamesStatus() {
		return eventNamesStatus;
	}

	/**
	 * @return the eventsStatus
	 */
	@Override
	public final StatusDTO getEventsStatus() {
		return eventsStatus;
	}

	/**
	 * @return the guildDetailsStatus
	 */
	@Override
	public final StatusDTO getGuildDetailsStatus() {
		return guildDetailsStatus;
	}

	/**
	 * @return the itemsStatus
	 */
	@Override
	public final StatusDTO getItemsStatus() {
		return itemsStatus;
	}

	/**
	 * @return the mapFloorStatus
	 */
	@Override
	public final StatusDTO getMapFloorStatus() {
		return mapFloorStatus;
	}

	/**
	 * @return the mapNamesStatus
	 */
	@Override
	public final StatusDTO getMapNamesStatus() {
		return mapNamesStatus;
	}

	/**
	 * @return the mapsStatus
	 */
	@Override
	public final StatusDTO getMapsStatus() {
		return mapsStatus;
	}

	/**
	 * @return the recipeDetailsStatus
	 */
	@Override
	public final StatusDTO getRecipeDetailsStatus() {
		return recipeDetailsStatus;
	}

	/**
	 * @return the recipesStatus
	 */
	@Override
	public final StatusDTO getRecipesStatus() {
		return recipesStatus;
	}

	/**
	 * @return the worldNamesStatus
	 */
	@Override
	public final StatusDTO getWorldNamesStatus() {
		return worldNamesStatus;
	}

	/**
	 * @return the matchDetailsStatus
	 */
	@Override
	public final StatusDTO getMatchDetailsStatus() {
		return matchDetailsStatus;
	}

	/**
	 * @return the matchesStatus
	 */
	@Override
	public final StatusDTO getMatchesStatus() {
		return matchesStatus;
	}

	/**
	 * @return the objectiveNamesStatus
	 */
	@Override
	public final StatusDTO getObjectiveNamesStatus() {
		return objectiveNamesStatus;
	}

	/**
	 * @return the tilesStatus
	 */
	@Override
	public final StatusDTO getTilesStatus() {
		return tilesStatus;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("statusCodesStatus", this.statusCodesStatus).add("statusStatus", this.statusStatus).add("buildStatus", this.buildStatus)
				.add("colorsStatus", this.colorsStatus).add("continentsStatus", this.continentsStatus).add("eventsDetailsStatus", this.eventsDetailsStatus)
				.add("eventNamesStatus", this.eventNamesStatus).add("eventsStatus", this.eventsStatus).add("guildDetailsStatus", this.guildDetailsStatus).add("itemsStatus", this.itemsStatus)
				.add("mapFloorStatus", this.mapFloorStatus).add("mapNamesStatus", this.mapNamesStatus).add("mapsStatus", this.mapsStatus).add("recipeDetailsStatus", recipeDetailsStatus)
				.add("recipesStatus", this.recipesStatus).add("worldNamesStatus", this.worldNamesStatus).add("matchDetailsStatus", this.matchDetailsStatus).add("matchesStatus", this.matchesStatus)
				.add("objectiveNamesStatus", this.objectiveNamesStatus).add("tilesStatus", this.tilesStatus).toString();
	}
}
