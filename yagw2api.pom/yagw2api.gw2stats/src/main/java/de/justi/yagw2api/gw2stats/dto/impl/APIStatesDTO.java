package de.justi.yagw2api.gw2stats.dto.impl;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStatesDTO;

final class APIStatesDTO implements IAPIStatesDTO {
	@SerializedName("http://gw2stats.net/api/status.json")
	@Since(1.0)
	private APIStateDTO statusCodesStatus;

	@SerializedName("http://gw2stats.net/api/status_codes.json")
	@Since(1.0)
	private APIStateDTO statusStatus;

	@SerializedName("https://api.guildwars2.com/v1/build.json")
	@Since(1.0)
	private APIStateDTO buildStatus;

	@SerializedName("https://api.guildwars2.com/v1/colors.json")
	@Since(1.0)
	private APIStateDTO colorsStatus;

	@SerializedName("https://api.guildwars2.com/v1/continents.json")
	@Since(1.0)
	private APIStateDTO continentsStatus;

	@SerializedName("https://api.guildwars2.com/v1/event_details.json")
	@Since(1.0)
	private APIStateDTO eventsDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/event_names.json")
	@Since(1.0)
	private APIStateDTO eventNamesStatus;

	@SerializedName("https://api.guildwars2.com/v1/events.json")
	@Since(1.0)
	private APIStateDTO eventsStatus;

	@SerializedName("https://api.guildwars2.com/v1/guild_details.json")
	@Since(1.0)
	private APIStateDTO guildDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/items.json")
	@Since(1.0)
	private APIStateDTO itemsStatus;

	@SerializedName("https://api.guildwars2.com/v1/map_floor.json")
	@Since(1.0)
	private APIStateDTO mapFloorStatus;

	@SerializedName("https://api.guildwars2.com/v1/map_names.json")
	@Since(1.0)
	private APIStateDTO mapNamesStatus;

	@SerializedName("https://api.guildwars2.com/v1/maps.json")
	@Since(1.0)
	private APIStateDTO mapsStatus;

	@SerializedName("https://api.guildwars2.com/v1/recipe_details.json")
	@Since(1.0)
	private APIStateDTO recipeDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/recipes.json")
	@Since(1.0)
	private APIStateDTO recipesStatus;

	@SerializedName("https://api.guildwars2.com/v1/world_names.json")
	@Since(1.0)
	private APIStateDTO worldNamesStatus;

	@SerializedName("https://api.guildwars2.com/v1/wvw/match_details.json")
	@Since(1.0)
	private APIStateDTO matchDetailsStatus;

	@SerializedName("https://api.guildwars2.com/v1/wvw/matches.json")
	@Since(1.0)
	private APIStateDTO matchesStatus;

	@SerializedName("https://api.guildwars2.com/v1/wvw/objective_names.json")
	@Since(1.0)
	private APIStateDTO objectiveNamesStatus;

	@SerializedName("https://tiles.guildwars2.com/")
	@Since(1.0)
	private APIStateDTO tilesStatus;

	/**
	 * @return the statusCodesStatus
	 */
	@Override
	public final APIStateDTO getStatusCodesStatus() {
		return statusCodesStatus;
	}

	/**
	 * @return the statusStatus
	 */
	@Override
	public final APIStateDTO getStatusStatus() {
		return statusStatus;
	}

	/**
	 * @return the buildStatus
	 */
	@Override
	public final APIStateDTO getBuildStatus() {
		return buildStatus;
	}

	/**
	 * @return the colorsStatus
	 */
	@Override
	public final APIStateDTO getColorsStatus() {
		return colorsStatus;
	}

	/**
	 * @return the continentsStatus
	 */
	@Override
	public final APIStateDTO getContinentsStatus() {
		return continentsStatus;
	}

	/**
	 * @return the eventsDetailsStatus
	 */
	@Override
	public final APIStateDTO getEventsDetailsStatus() {
		return eventsDetailsStatus;
	}

	/**
	 * @return the eventNamesStatus
	 */
	@Override
	public final APIStateDTO getEventNamesStatus() {
		return eventNamesStatus;
	}

	/**
	 * @return the eventsStatus
	 */
	@Override
	public final APIStateDTO getEventsStatus() {
		return eventsStatus;
	}

	/**
	 * @return the guildDetailsStatus
	 */
	@Override
	public final APIStateDTO getGuildDetailsStatus() {
		return guildDetailsStatus;
	}

	/**
	 * @return the itemsStatus
	 */
	@Override
	public final APIStateDTO getItemsStatus() {
		return itemsStatus;
	}

	/**
	 * @return the mapFloorStatus
	 */
	@Override
	public final APIStateDTO getMapFloorStatus() {
		return mapFloorStatus;
	}

	/**
	 * @return the mapNamesStatus
	 */
	@Override
	public final APIStateDTO getMapNamesStatus() {
		return mapNamesStatus;
	}

	/**
	 * @return the mapsStatus
	 */
	@Override
	public final APIStateDTO getMapsStatus() {
		return mapsStatus;
	}

	/**
	 * @return the recipeDetailsStatus
	 */
	@Override
	public final APIStateDTO getRecipeDetailsStatus() {
		return recipeDetailsStatus;
	}

	/**
	 * @return the recipesStatus
	 */
	@Override
	public final APIStateDTO getRecipesStatus() {
		return recipesStatus;
	}

	/**
	 * @return the worldNamesStatus
	 */
	@Override
	public final APIStateDTO getWorldNamesStatus() {
		return worldNamesStatus;
	}

	/**
	 * @return the matchDetailsStatus
	 */
	@Override
	public final APIStateDTO getMatchDetailsStatus() {
		return matchDetailsStatus;
	}

	/**
	 * @return the matchesStatus
	 */
	@Override
	public final APIStateDTO getMatchesStatus() {
		return matchesStatus;
	}

	/**
	 * @return the objectiveNamesStatus
	 */
	@Override
	public final APIStateDTO getObjectiveNamesStatus() {
		return objectiveNamesStatus;
	}

	/**
	 * @return the tilesStatus
	 */
	@Override
	public final APIStateDTO getTilesStatus() {
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
