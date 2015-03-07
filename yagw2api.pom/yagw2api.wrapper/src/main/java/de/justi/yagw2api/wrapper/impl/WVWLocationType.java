package de.justi.yagw2api.wrapper.impl;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IWVWLocationType;
import de.justi.yagw2api.wrapper.IWVWMapType;
import de.justi.yagw2api.wrapper.IWVWObjectiveType;

public enum WVWLocationType implements IWVWLocationType {
	// eternal battlegrounds -> spawns
	/**
	 * red spawn
	 */
	RED_WORLD_HILL(WVWMapType.CENTER),
	/**
	 * green spawn
	 */
	GREEN_WORLD_HILL(WVWMapType.CENTER),
	/**
	 * blue spawn
	 */
	BLUE_WORLD_HILL(WVWMapType.CENTER),
	// eternal battleground -> objectives
	RED_WORLD_OVERLOOK(1, WVWObjectiveType.KEEP, WVWMapType.CENTER),
	BLUE_WORLD_VALLEY(2, WVWObjectiveType.KEEP, WVWMapType.CENTER),
	GREEN_WORLD_LOWLANDS(3, WVWObjectiveType.KEEP, WVWMapType.CENTER),
	GOLANTA_CLEARING(4, WVWObjectiveType.CAMP, WVWMapType.CENTER),
	PANGLOSS_RISE(5, WVWObjectiveType.CAMP, WVWMapType.CENTER),
	SPELDAN_CLEARCUT(6, WVWObjectiveType.CAMP, WVWMapType.CENTER),
	DANELON_PASSAGE(7, WVWObjectiveType.CAMP, WVWMapType.CENTER),
	UMBERGLADE_WOODS(8, WVWObjectiveType.CAMP, WVWMapType.CENTER),
	STONEMIST_CASTLE(9, WVWObjectiveType.CASTLE, WVWMapType.CENTER),
	ROGUES_QUARRY(10, WVWObjectiveType.CAMP, WVWMapType.CENTER),
	ALDONS_LEDGE(11, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	WILDCREEK_RUN(12, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	JERRIFERS_SLOUGH(13, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	KLOVAN_GULLY(14, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	LANGOR_GULCH(15, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	QUENTIN_LAKE(16, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	MENDONS_GAP(17, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	ANZALIAS_PASS(18, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	OGRE_WATCH(19, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	VELOKA_SLOPE(20, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	DURIOS_GULCH(21, WVWObjectiveType.TOWER, WVWMapType.CENTER),
	BRAVOST_ESCARPMENT(22, WVWObjectiveType.TOWER, WVWMapType.CENTER),

	// red -> spawn
	/**
	 * red spawn
	 */
	RED_WORLD_CITADEL(WVWMapType.RED),
	/**
	 * green spawn
	 */
	RED_WORLD_GREEN_BORDER(WVWMapType.RED),
	/**
	 * blue spawn
	 */
	RED_WORLD_BLUE_BORDER(WVWMapType.RED),
	// red -> objectives
	ETHERON_HILLS(32, WVWObjectiveType.KEEP, WVWMapType.RED),
	DREAMING_BAY(33, WVWObjectiveType.KEEP, WVWMapType.RED),
	VICTORS_LODGE(34, WVWObjectiveType.CAMP, WVWMapType.RED),
	GREENBRIAR(35, WVWObjectiveType.TOWER, WVWMapType.RED),
	BLUELAKE(36, WVWObjectiveType.TOWER, WVWMapType.RED),
	RED_WORLD_GARRISON(37, WVWObjectiveType.KEEP, WVWMapType.RED),
	LONGVIEW(38, WVWObjectiveType.TOWER, WVWMapType.RED),
	THE_GODSWORD(39, WVWObjectiveType.CAMP, WVWMapType.RED),
	CLIFFSIDE(40, WVWObjectiveType.TOWER, WVWMapType.RED),
	BLUEWATER_LOWLANDS(50, WVWObjectiveType.CAMP, WVWMapType.RED),
	ASTRALHOLME(51, WVWObjectiveType.CAMP, WVWMapType.RED),
	ARAHS_HOPE(52, WVWObjectiveType.CAMP, WVWMapType.RED),
	GREENVALE_REFUGE(53, WVWObjectiveType.CAMP, WVWMapType.RED),
	RED_TEMPLE_OF_LOST_PRAYERS(62, WVWObjectiveType.RUINS, WVWMapType.RED),
	RED_BATTLES_HOLLOW(63, WVWObjectiveType.RUINS, WVWMapType.RED),
	RED_BAUERS_ESTATE(64, WVWObjectiveType.RUINS, WVWMapType.RED),
	RED_ORCHARD_OVERLOOK(65, WVWObjectiveType.RUINS, WVWMapType.RED),
	RED_CARVERS_ASCENT(66, WVWObjectiveType.RUINS, WVWMapType.RED),

	// blue -> spawn
	/**
	 * blue spawn
	 */
	BLUE_WORLD_CITADEL(WVWMapType.BLUE),
	/**
	 * green spawn
	 */
	BLUE_WORLD_GREEN_BORDER(WVWMapType.BLUE),
	/**
	 * red spawn
	 */
	BLUE_WORLD_RED_BORDER(WVWMapType.BLUE),
	// blue -> objectives
	CHAMPIONS_DEMENSE(24, WVWObjectiveType.CAMP, WVWMapType.BLUE),
	BLUE_WORLD_GARRISON(23, WVWObjectiveType.KEEP, WVWMapType.BLUE),
	REDBRIAR(25, WVWObjectiveType.TOWER, WVWMapType.BLUE),
	GREENLAKE(26, WVWObjectiveType.TOWER, WVWMapType.BLUE),
	ASCENSION_BAY(27, WVWObjectiveType.KEEP, WVWMapType.BLUE),
	DAWNS_EYRIE(28, WVWObjectiveType.TOWER, WVWMapType.BLUE),
	THE_SPIRITHOLME(29, WVWObjectiveType.CAMP, WVWMapType.BLUE),
	WOODHAVEN(30, WVWObjectiveType.TOWER, WVWMapType.BLUE),
	ASKALION_HILLS(31, WVWObjectiveType.KEEP, WVWMapType.BLUE),
	GODSLORE(58, WVWObjectiveType.CAMP, WVWMapType.BLUE),
	REDVALE_REFUGE(59, WVWObjectiveType.CAMP, WVWMapType.BLUE),
	STARGROVE(60, WVWObjectiveType.CAMP, WVWMapType.BLUE),
	GREEMWATER_LOWLANDS(61, WVWObjectiveType.CAMP, WVWMapType.BLUE),
	BLUE_CARVERS_ASCENT(67, WVWObjectiveType.RUINS, WVWMapType.BLUE),
	BLUE_ORCHARD_OVERLOOK(68, WVWObjectiveType.RUINS, WVWMapType.BLUE),
	BLUE_BAUERS_ESTATE(69, WVWObjectiveType.RUINS, WVWMapType.BLUE),
	BLUE_BATTLES_HOLLOW(70, WVWObjectiveType.RUINS, WVWMapType.BLUE),
	BLUE_TEMPLE_OF_LOST_PRAYERS(71, WVWObjectiveType.RUINS, WVWMapType.BLUE),

	// green -> spawn
	/**
	 * green spawn
	 */
	GREEN_WORLD_CITADEL(WVWMapType.GREEN),
	/**
	 * blue spawn
	 */
	GREEN_WORLD_BLUE_BORDER(WVWMapType.GREEN),
	/**
	 * red spawn
	 */
	GREEN_WORLD_RED_BORDER(WVWMapType.GREEN),
	// green -> objectives
	SHADARAN_HILLS(41, WVWObjectiveType.KEEP, WVWMapType.GREEN),
	REDLAKE(42, WVWObjectiveType.TOWER, WVWMapType.GREEN),
	HEROS_LODGE(43, WVWObjectiveType.CAMP, WVWMapType.GREEN),
	DREADFALL_BAY(44, WVWObjectiveType.KEEP, WVWMapType.GREEN),
	BLUEBRIAR(45, WVWObjectiveType.TOWER, WVWMapType.GREEN),
	GREEN_WORLD_GARRISON(46, WVWObjectiveType.KEEP, WVWMapType.GREEN),
	SUNNYHILL(47, WVWObjectiveType.TOWER, WVWMapType.GREEN),
	FAITHLEAP(48, WVWObjectiveType.CAMP, WVWMapType.GREEN),
	BLUEVALE_REFUGE(49, WVWObjectiveType.CAMP, WVWMapType.GREEN),
	FOGHAVEN(54, WVWObjectiveType.CAMP, WVWMapType.GREEN),
	REDWATER_LOWLANDS(55, WVWObjectiveType.CAMP, WVWMapType.GREEN),
	THE_TITANPAW(56, WVWObjectiveType.CAMP, WVWMapType.GREEN),
	CRAGTOP(57, WVWObjectiveType.TOWER, WVWMapType.GREEN),
	GREEN_CARVERS_ASCENT(72, WVWObjectiveType.RUINS, WVWMapType.GREEN),
	GREEN_ORCHARD_OVERLOOK(73, WVWObjectiveType.RUINS, WVWMapType.GREEN),
	GREEN_BAUERS_ESTATE(74, WVWObjectiveType.RUINS, WVWMapType.GREEN),
	GREEN_BATTLES_HOLLOW(75, WVWObjectiveType.RUINS, WVWMapType.GREEN),
	GREEN_TEMPLE_OF_LOST_PRAYERS(76, WVWObjectiveType.RUINS, WVWMapType.GREEN);

	private static final IWVWMapType CENTER_MAPTYPE = WVWMapType.CENTER;
	private static final IWVWMapType RED_MAPTYPE = WVWMapType.RED;
	private static final IWVWMapType GREEN_MAPTYPE = WVWMapType.GREEN;
	private static final IWVWMapType BLUE_MAPTYPE = WVWMapType.BLUE;

	private static final Logger LOGGER = LoggerFactory.getLogger(WVWLocationType.class);
	private static final Map<Integer, IWVWLocationType> LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID;
	private static final Map<IWVWMapType, Set<IWVWLocationType>> LOCATIONTYPES_MAPPED_BY_MAPTYPE;
	static {
		final ImmutableMap.Builder<Integer, IWVWLocationType> mapByObjectiveIdBuilder = ImmutableMap.builder();
		final ImmutableSet.Builder<IWVWLocationType> centerLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<IWVWLocationType> redLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<IWVWLocationType> greenLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<IWVWLocationType> blueLocationSetBuilder = ImmutableSet.builder();
		for (WVWLocationType location : WVWLocationType.values()) {
			checkState(location.getObjectiveId().isPresent() == location.getObjectiveType().isPresent());
			if (location.getObjectiveId().isPresent()) {
				mapByObjectiveIdBuilder.put(location.getObjectiveId().get(), location);
			}
			if (location.getMapType().isCenter()) {
				centerLocationSetBuilder.add(location);
			} else if (location.getMapType().isBlue()) {
				blueLocationSetBuilder.add(location);
			} else if (location.getMapType().isGreen()) {
				greenLocationSetBuilder.add(location);
			} else if (location.getMapType().isRed()) {
				redLocationSetBuilder.add(location);
			} else {
				throw new IllegalStateException("Invalid map type(" + location.getMapType() + ") for location " + location);
			}
		}
		LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID = mapByObjectiveIdBuilder.build();
		LOCATIONTYPES_MAPPED_BY_MAPTYPE = ImmutableMap.<IWVWMapType, Set<IWVWLocationType>> builder().put(RED_MAPTYPE, redLocationSetBuilder.build())
				.put(GREEN_MAPTYPE, greenLocationSetBuilder.build()).put(BLUE_MAPTYPE, blueLocationSetBuilder.build()).put(CENTER_MAPTYPE, centerLocationSetBuilder.build())
				.build();

		checkState(WVWLocationType.forMapTyp(CENTER_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(BLUE_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(GREEN_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(RED_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() == WVWLocationType.forMapTyp(RED_MAPTYPE).get().size(), "green("
				+ WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() + ")==red(" + WVWLocationType.forMapTyp(RED_MAPTYPE).get().size() + ")");
		checkState(WVWLocationType.forMapTyp(BLUE_MAPTYPE).get().size() == WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size(), "blue("
				+ WVWLocationType.forMapTyp(BLUE_MAPTYPE).get().size() + ")==green(" + WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() + ")");

		LOGGER.trace("Initialized " + WVWLocationType.class.getSimpleName());
	}

	private final IWVWMapType mapType;
	private final Optional<Integer> objectiveId;
	private final Optional<IWVWObjectiveType> objectiveType;

	private WVWLocationType(final IWVWMapType type) {
		this(null, null, checkNotNull(type));
	}

	private WVWLocationType(final Integer objectiveId, final IWVWObjectiveType objectiveType, final IWVWMapType type) {
		this.objectiveId = Optional.fromNullable(objectiveId);
		this.objectiveType = Optional.fromNullable(objectiveType);
		checkNotNull(type);
		this.mapType = type;
	}

	@Override
	public Optional<IWVWObjectiveType> getObjectiveType() {
		return this.objectiveType;
	}

	@Override
	public Optional<String> getLabel(final Locale locale) {
		checkNotNull(locale);
		final ResourceBundle bundle = ResourceBundle.getBundle("locations", locale);
		try {
			return Optional.of(bundle.getString(this.name()));
		} catch (MissingResourceException e) {
			LOGGER.error("Missing translation of " + this.name() + " for " + locale, e);
			return Optional.absent();
		}
	}

	@Override
	public Optional<Integer> getObjectiveId() {
		return this.objectiveId;
	}

	public static Optional<IWVWLocationType> forObjectiveId(final int objectiveId) {
		final Optional<IWVWLocationType> location = Optional.fromNullable(LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID.get(objectiveId));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Determined location " + location + " for " + objectiveId);
		}
		return location;
	}

	public static Optional<Set<IWVWLocationType>> forMapTyp(final IWVWMapType mapType) {
		return Optional.fromNullable(LOCATIONTYPES_MAPPED_BY_MAPTYPE.get(mapType));
	}

	@Override
	public IWVWMapType getMapType() {
		return this.mapType;
	}

	@Override
	public boolean isObjectiveLocation() {
		return this.getObjectiveId().isPresent() || this.getObjectiveType().isPresent();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("label", this.getLabel(YAGW2APIArenanet.INSTANCE.getCurrentLocale())).add("isObjectiveLocation", this.isObjectiveLocation())
				.add("objectiveId", this.getObjectiveId()).add("objectiveType", this.getObjectiveType()).add("mapType", this.getMapType()).toString();
	}
}
