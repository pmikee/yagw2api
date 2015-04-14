package de.justi.yagw2api.wrapper.domain.wvw;

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

public enum DefaultWVWLocationType implements WVWLocationType {
	// eternal battlegrounds -> spawns
	/**
	 * red spawn
	 */
	RED_WORLD_HILL(DefaultWVWMapType.CENTER),
	/**
	 * green spawn
	 */
	GREEN_WORLD_HILL(DefaultWVWMapType.CENTER),
	/**
	 * blue spawn
	 */
	BLUE_WORLD_HILL(DefaultWVWMapType.CENTER),
	// eternal battleground -> objectives
	RED_WORLD_OVERLOOK(1, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.CENTER),
	BLUE_WORLD_VALLEY(2, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.CENTER),
	GREEN_WORLD_LOWLANDS(3, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.CENTER),
	GOLANTA_CLEARING(4, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.CENTER),
	PANGLOSS_RISE(5, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.CENTER),
	SPELDAN_CLEARCUT(6, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.CENTER),
	DANELON_PASSAGE(7, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.CENTER),
	UMBERGLADE_WOODS(8, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.CENTER),
	STONEMIST_CASTLE(9, DefaultWVWObjectiveType.CASTLE, DefaultWVWMapType.CENTER),
	ROGUES_QUARRY(10, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.CENTER),
	ALDONS_LEDGE(11, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	WILDCREEK_RUN(12, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	JERRIFERS_SLOUGH(13, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	KLOVAN_GULLY(14, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	LANGOR_GULCH(15, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	QUENTIN_LAKE(16, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	MENDONS_GAP(17, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	ANZALIAS_PASS(18, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	OGRE_WATCH(19, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	VELOKA_SLOPE(20, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	DURIOS_GULCH(21, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),
	BRAVOST_ESCARPMENT(22, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.CENTER),

	// red -> spawn
	/**
	 * red spawn
	 */
	RED_WORLD_CITADEL(DefaultWVWMapType.RED),
	/**
	 * green spawn
	 */
	RED_WORLD_GREEN_BORDER(DefaultWVWMapType.RED),
	/**
	 * blue spawn
	 */
	RED_WORLD_BLUE_BORDER(DefaultWVWMapType.RED),
	// red -> objectives
	ETHERON_HILLS(32, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.RED),
	DREAMING_BAY(33, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.RED),
	VICTORS_LODGE(34, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.RED),
	GREENBRIAR(35, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.RED),
	BLUELAKE(36, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.RED),
	RED_WORLD_GARRISON(37, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.RED),
	LONGVIEW(38, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.RED),
	THE_GODSWORD(39, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.RED),
	CLIFFSIDE(40, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.RED),
	BLUEWATER_LOWLANDS(50, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.RED),
	ASTRALHOLME(51, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.RED),
	ARAHS_HOPE(52, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.RED),
	GREENVALE_REFUGE(53, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.RED),
	RED_TEMPLE_OF_LOST_PRAYERS(62, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.RED),
	RED_BATTLES_HOLLOW(63, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.RED),
	RED_BAUERS_ESTATE(64, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.RED),
	RED_ORCHARD_OVERLOOK(65, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.RED),
	RED_CARVERS_ASCENT(66, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.RED),

	// blue -> spawn
	/**
	 * blue spawn
	 */
	BLUE_WORLD_CITADEL(DefaultWVWMapType.BLUE),
	/**
	 * green spawn
	 */
	BLUE_WORLD_GREEN_BORDER(DefaultWVWMapType.BLUE),
	/**
	 * red spawn
	 */
	BLUE_WORLD_RED_BORDER(DefaultWVWMapType.BLUE),
	// blue -> objectives
	CHAMPIONS_DEMENSE(24, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.BLUE),
	BLUE_WORLD_GARRISON(23, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.BLUE),
	REDBRIAR(25, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.BLUE),
	GREENLAKE(26, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.BLUE),
	ASCENSION_BAY(27, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.BLUE),
	DAWNS_EYRIE(28, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.BLUE),
	THE_SPIRITHOLME(29, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.BLUE),
	WOODHAVEN(30, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.BLUE),
	ASKALION_HILLS(31, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.BLUE),
	GODSLORE(58, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.BLUE),
	REDVALE_REFUGE(59, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.BLUE),
	STARGROVE(60, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.BLUE),
	GREEMWATER_LOWLANDS(61, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.BLUE),
	BLUE_CARVERS_ASCENT(67, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.BLUE),
	BLUE_ORCHARD_OVERLOOK(68, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.BLUE),
	BLUE_BAUERS_ESTATE(69, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.BLUE),
	BLUE_BATTLES_HOLLOW(70, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.BLUE),
	BLUE_TEMPLE_OF_LOST_PRAYERS(71, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.BLUE),

	// green -> spawn
	/**
	 * green spawn
	 */
	GREEN_WORLD_CITADEL(DefaultWVWMapType.GREEN),
	/**
	 * blue spawn
	 */
	GREEN_WORLD_BLUE_BORDER(DefaultWVWMapType.GREEN),
	/**
	 * red spawn
	 */
	GREEN_WORLD_RED_BORDER(DefaultWVWMapType.GREEN),
	// green -> objectives
	SHADARAN_HILLS(41, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.GREEN),
	REDLAKE(42, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.GREEN),
	HEROS_LODGE(43, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.GREEN),
	DREADFALL_BAY(44, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.GREEN),
	BLUEBRIAR(45, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.GREEN),
	GREEN_WORLD_GARRISON(46, DefaultWVWObjectiveType.KEEP, DefaultWVWMapType.GREEN),
	SUNNYHILL(47, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.GREEN),
	FAITHLEAP(48, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.GREEN),
	BLUEVALE_REFUGE(49, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.GREEN),
	FOGHAVEN(54, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.GREEN),
	REDWATER_LOWLANDS(55, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.GREEN),
	THE_TITANPAW(56, DefaultWVWObjectiveType.CAMP, DefaultWVWMapType.GREEN),
	CRAGTOP(57, DefaultWVWObjectiveType.TOWER, DefaultWVWMapType.GREEN),
	GREEN_CARVERS_ASCENT(72, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.GREEN),
	GREEN_ORCHARD_OVERLOOK(73, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.GREEN),
	GREEN_BAUERS_ESTATE(74, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.GREEN),
	GREEN_BATTLES_HOLLOW(75, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.GREEN),
	GREEN_TEMPLE_OF_LOST_PRAYERS(76, DefaultWVWObjectiveType.RUINS, DefaultWVWMapType.GREEN);

	private static final WVWMapType CENTER_MAPTYPE = DefaultWVWMapType.CENTER;
	private static final WVWMapType RED_MAPTYPE = DefaultWVWMapType.RED;
	private static final WVWMapType GREEN_MAPTYPE = DefaultWVWMapType.GREEN;
	private static final WVWMapType BLUE_MAPTYPE = DefaultWVWMapType.BLUE;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWVWLocationType.class);
	private static final Map<Integer, WVWLocationType> LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID;
	private static final Map<WVWMapType, Set<WVWLocationType>> LOCATIONTYPES_MAPPED_BY_MAPTYPE;
	static {
		final ImmutableMap.Builder<Integer, WVWLocationType> mapByObjectiveIdBuilder = ImmutableMap.builder();
		final ImmutableSet.Builder<WVWLocationType> centerLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<WVWLocationType> redLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<WVWLocationType> greenLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<WVWLocationType> blueLocationSetBuilder = ImmutableSet.builder();
		for (DefaultWVWLocationType location : DefaultWVWLocationType.values()) {
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
		LOCATIONTYPES_MAPPED_BY_MAPTYPE = ImmutableMap.<WVWMapType, Set<WVWLocationType>> builder().put(RED_MAPTYPE, redLocationSetBuilder.build())
				.put(GREEN_MAPTYPE, greenLocationSetBuilder.build()).put(BLUE_MAPTYPE, blueLocationSetBuilder.build()).put(CENTER_MAPTYPE, centerLocationSetBuilder.build())
				.build();

		checkState(DefaultWVWLocationType.forMapTyp(CENTER_MAPTYPE).isPresent());
		checkState(DefaultWVWLocationType.forMapTyp(BLUE_MAPTYPE).isPresent());
		checkState(DefaultWVWLocationType.forMapTyp(GREEN_MAPTYPE).isPresent());
		checkState(DefaultWVWLocationType.forMapTyp(RED_MAPTYPE).isPresent());
		checkState(DefaultWVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() == DefaultWVWLocationType.forMapTyp(RED_MAPTYPE).get().size(), "green("
				+ DefaultWVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() + ")==red(" + DefaultWVWLocationType.forMapTyp(RED_MAPTYPE).get().size() + ")");
		checkState(DefaultWVWLocationType.forMapTyp(BLUE_MAPTYPE).get().size() == DefaultWVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size(), "blue("
				+ DefaultWVWLocationType.forMapTyp(BLUE_MAPTYPE).get().size() + ")==green(" + DefaultWVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() + ")");

		LOGGER.trace("Initialized " + DefaultWVWLocationType.class.getSimpleName());
	}

	private final WVWMapType mapType;
	private final Optional<Integer> objectiveId;
	private final Optional<WVWObjectiveType> objectiveType;

	private DefaultWVWLocationType(final WVWMapType type) {
		this(null, null, checkNotNull(type));
	}

	private DefaultWVWLocationType(final Integer objectiveId, final WVWObjectiveType objectiveType, final WVWMapType type) {
		this.objectiveId = Optional.fromNullable(objectiveId);
		this.objectiveType = Optional.fromNullable(objectiveType);
		checkNotNull(type);
		this.mapType = type;
	}

	@Override
	public Optional<WVWObjectiveType> getObjectiveType() {
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

	public static Optional<WVWLocationType> forObjectiveId(final int objectiveId) {
		final Optional<WVWLocationType> location = Optional.fromNullable(LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID.get(objectiveId));
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Determined location " + location + " for " + objectiveId);
		}
		return location;
	}

	public static Optional<Set<WVWLocationType>> forMapTyp(final WVWMapType mapType) {
		return Optional.fromNullable(LOCATIONTYPES_MAPPED_BY_MAPTYPE.get(mapType));
	}

	@Override
	public WVWMapType getMapType() {
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
