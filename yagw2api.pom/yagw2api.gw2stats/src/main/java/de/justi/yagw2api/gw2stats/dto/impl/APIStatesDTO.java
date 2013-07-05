package de.justi.yagw2api.gw2stats.dto.impl;

import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSortedMap;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import de.justi.yagw2api.gw2stats.dto.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.dto.IAPIStatesDTO;

final class APIStatesDTO implements IAPIStatesDTO {

	@SerializedName("http://gw2stats.net/api/status.json")
	@Since(1.0)
	private APIStateDTO stateCodesState;

	@SerializedName("http://gw2stats.net/api/status_codes.json")
	@Since(1.0)
	private APIStateDTO stateState;

	@SerializedName("https://api.guildwars2.com/v1/build.json")
	@Since(1.0)
	private APIStateDTO buildState;

	@SerializedName("https://api.guildwars2.com/v1/colors.json")
	@Since(1.0)
	private APIStateDTO colorsState;

	@SerializedName("https://api.guildwars2.com/v1/continents.json")
	@Since(1.0)
	private APIStateDTO continentsState;

	@SerializedName("https://api.guildwars2.com/v1/event_details.json")
	@Since(1.0)
	private APIStateDTO eventsDetailsState;

	@SerializedName("https://api.guildwars2.com/v1/event_names.json")
	@Since(1.0)
	private APIStateDTO eventNamesState;

	@SerializedName("https://api.guildwars2.com/v1/events.json")
	@Since(1.0)
	private APIStateDTO eventsState;

	@SerializedName("https://api.guildwars2.com/v1/guild_details.json?guild_name=Arenanet")
	@Since(1.0)
	private APIStateDTO guildDetailsState;

	@SerializedName("https://api.guildwars2.com/v1/items.json")
	@Since(1.0)
	private APIStateDTO itemsState;

	@SerializedName("https://api.guildwars2.com/v1/item_details.json?item_id=14484")
	@Since(1.0)
	private APIStateDTO itemDetailsState;

	@SerializedName("https://api.guildwars2.com/v1/map_floor.json")
	@Since(1.0)
	private APIStateDTO mapFloorState;

	@SerializedName("https://api.guildwars2.com/v1/map_names.json")
	@Since(1.0)
	private APIStateDTO mapNamesState;

	@SerializedName("https://api.guildwars2.com/v1/maps.json")
	@Since(1.0)
	private APIStateDTO mapsState;

	@SerializedName("https://api.guildwars2.com/v1/recipe_details.json?recipe_id=1972")
	@Since(1.0)
	private APIStateDTO recipeDetailsState;

	@SerializedName("https://api.guildwars2.com/v1/recipes.json")
	@Since(1.0)
	private APIStateDTO recipesState;

	@SerializedName("https://api.guildwars2.com/v1/world_names.json")
	@Since(1.0)
	private APIStateDTO worldNamesState;

	@SerializedName("https://api.guildwars2.com/v1/wvw/match_details.json?match_id=1-3")
	@Since(1.0)
	private APIStateDTO matchDetailsState;

	@SerializedName("https://api.guildwars2.com/v1/wvw/matches.json")
	@Since(1.0)
	private APIStateDTO matchesState;

	@SerializedName("https://api.guildwars2.com/v1/wvw/objective_names.json")
	@Since(1.0)
	private APIStateDTO objectiveNamesState;

	@SerializedName("https://tiles.guildwars2.com/")
	@Since(1.0)
	private APIStateDTO tilesState;

	private volatile transient SortedMap<String, IAPIStateDTO> statesMappedByURL = null;

	/**
	 * @return the statusCodesStatus
	 */
	@Override
	public final IAPIStateDTO getStateCodesState() {
		return stateCodesState;
	}

	/**
	 * @return the statusStatus
	 */
	@Override
	public final IAPIStateDTO getStatesState() {
		return stateState;
	}

	/**
	 * @return the buildStatus
	 */
	@Override
	public final IAPIStateDTO getBuildState() {
		return buildState;
	}

	/**
	 * @return the colorsStatus
	 */
	@Override
	public final IAPIStateDTO getColorsState() {
		return colorsState;
	}

	/**
	 * @return the continentsStatus
	 */
	@Override
	public final IAPIStateDTO getContinentsState() {
		return continentsState;
	}

	/**
	 * @return the eventsDetailsStatus
	 */
	@Override
	public final IAPIStateDTO getEventsDetailsState() {
		return eventsDetailsState;
	}

	/**
	 * @return the eventNamesStatus
	 */
	@Override
	public final IAPIStateDTO getEventNamesState() {
		return eventNamesState;
	}

	/**
	 * @return the eventsStatus
	 */
	@Override
	public final IAPIStateDTO getEventsState() {
		return eventsState;
	}

	/**
	 * @return the guildDetailsStatus
	 */
	@Override
	public final IAPIStateDTO getGuildDetailsState() {
		return guildDetailsState;
	}

	/**
	 * @return the itemsStatus
	 */
	@Override
	public final IAPIStateDTO getItemsState() {
		return itemsState;
	}

	/**
	 * @return the itemDetailsState
	 */
	@Override
	public final IAPIStateDTO getItemDetailsState() {
		return itemDetailsState;
	}

	/**
	 * @return the mapFloorStatus
	 */
	@Override
	public final IAPIStateDTO getMapFloorState() {
		return mapFloorState;
	}

	/**
	 * @return the mapNamesStatus
	 */
	@Override
	public final IAPIStateDTO getMapNamesState() {
		return mapNamesState;
	}

	/**
	 * @return the mapsStatus
	 */
	@Override
	public final IAPIStateDTO getMapsState() {
		return mapsState;
	}

	/**
	 * @return the recipeDetailsStatus
	 */
	@Override
	public final IAPIStateDTO getRecipeDetailsState() {
		return recipeDetailsState;
	}

	/**
	 * @return the recipesStatus
	 */
	@Override
	public final IAPIStateDTO getRecipesState() {
		return recipesState;
	}

	/**
	 * @return the worldNamesStatus
	 */
	@Override
	public final IAPIStateDTO getWorldNamesState() {
		return worldNamesState;
	}

	/**
	 * @return the matchDetailsStatus
	 */
	@Override
	public final IAPIStateDTO getMatchDetailsState() {
		return matchDetailsState;
	}

	/**
	 * @return the matchesStatus
	 */
	@Override
	public final IAPIStateDTO getMatchesState() {
		return matchesState;
	}

	/**
	 * @return the objectiveNamesStatus
	 */
	@Override
	public final IAPIStateDTO getObjectiveNamesState() {
		return objectiveNamesState;
	}

	/**
	 * @return the tilesStatus
	 */
	@Override
	public final IAPIStateDTO getTilesState() {
		return tilesState;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("statusCodesStatus", this.stateCodesState).add("statusStatus", this.stateState).add("buildStatus", this.buildState)
				.add("colorsStatus", this.colorsState).add("continentsStatus", this.continentsState).add("eventsDetailsStatus", this.eventsDetailsState).add("eventNamesStatus", this.eventNamesState)
				.add("eventsStatus", this.eventsState).add("guildDetailsStatus", this.guildDetailsState).add("itemsStatus", this.itemsState).add("itemDetailsState", this.itemDetailsState)
				.add("mapFloorStatus", this.mapFloorState).add("mapNamesStatus", this.mapNamesState).add("mapsStatus", this.mapsState).add("recipeDetailsStatus", recipeDetailsState)
				.add("recipesStatus", this.recipesState).add("worldNamesStatus", this.worldNamesState).add("matchDetailsStatus", this.matchDetailsState).add("matchesStatus", this.matchesState)
				.add("objectiveNamesStatus", this.objectiveNamesState).add("tilesStatus", this.tilesState).toString();
	}

	@Override
	public SortedMap<String, IAPIStateDTO> getStatesMapedByURL() {
		if (this.statesMappedByURL == null) {
			synchronized (this) {
				if (this.statesMappedByURL == null) {
					final SortedMap<String, IAPIStateDTO> statesMap = new TreeMap<String, IAPIStateDTO>();
					statesMap.put("http://gw2stats.net/api/status.json", this.getStatesState());
					statesMap.put("http://gw2stats.net/api/status_codes.json", this.getStateCodesState());
					statesMap.put("https://api.guildwars2.com/v1/build.json", this.getBuildState());
					statesMap.put("https://api.guildwars2.com/v1/colors.json", this.getColorsState());
					statesMap.put("https://api.guildwars2.com/v1/continents.json", this.getContinentsState());
					statesMap.put("https://api.guildwars2.com/v1/event_details.json", this.getEventsDetailsState());
					statesMap.put("https://api.guildwars2.com/v1/event_names.json", this.getEventNamesState());
					statesMap.put("https://api.guildwars2.com/v1/events.json", this.getEventsState());
					statesMap.put("https://api.guildwars2.com/v1/guild_details.json", this.getGuildDetailsState());
					statesMap.put("https://api.guildwars2.com/v1/items.json", this.getItemsState());
					statesMap.put("https://api.guildwars2.com/v1/item_details.json", this.getItemDetailsState());
					statesMap.put("https://api.guildwars2.com/v1/map_floor.json", this.getMapFloorState());
					statesMap.put("https://api.guildwars2.com/v1/map_names.json", this.getMapNamesState());
					statesMap.put("https://api.guildwars2.com/v1/maps.json", this.getMapsState());
					statesMap.put("https://api.guildwars2.com/v1/recipe_details.json", this.getRecipeDetailsState());
					statesMap.put("https://api.guildwars2.com/v1/recipes.json", this.getRecipesState());
					statesMap.put("https://api.guildwars2.com/v1/world_names.json", this.getWorldNamesState());
					statesMap.put("https://api.guildwars2.com/v1/wvw/match_details.json", this.getMatchDetailsState());
					statesMap.put("https://api.guildwars2.com/v1/wvw/matches.json", this.getMatchesState());
					statesMap.put("https://api.guildwars2.com/v1/wvw/objective_names.json", this.getWorldNamesState());
					statesMap.put("https://tiles.guildwars2.com/", this.getTilesState());
					this.statesMappedByURL = ImmutableSortedMap.copyOf(statesMap);
				}
			}
		}
		return this.statesMappedByURL;
	}
}
