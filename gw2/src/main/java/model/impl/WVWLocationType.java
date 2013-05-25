package model.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import model.IWVWLocationType;
import model.IWVWMapType;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * <ul>
 * <li>id:1 = “Overlook”,25</li>
 * <li>id:2 = “Valley”,25</li>
 * <li>id:3 = “Lowlands”,25</li>
 * <li>id:4 = “Golanta Clearing”,5</li>
 * <li>id:5 = “Pangloss Rise”,5</li>
 * <li>id:6 = “Speldan Clearcut”,5</li>
 * <li>id:7 = “Danelon Passage”,5</li>
 * <li>id:8 = “Umberglade Woods”,5</li>
 * <li>id:9 = “Stonemist Castle”,35</li>
 * <li>id:10 = “Rogue’s Quarry”,5</li>
 * <li>id:11 = “Aldon’s Ledge”,10</li>
 * <li>id:12 = “Wildcreek Run”,10</li>
 * <li>id:13 = “Jerrifer’s Slough”,10</li>
 * <li>id:14 = “Klovan Gully”,10</li>
 * <li>id:15 = “Langor Gulch”,10</li>
 * <li>id:16 = “Quentin Lake”,10</li>
 * <li>id:17 = “Mendon’s Gap”,10</li>
 * <li>id:18 = “Anzalias Pass”,10</li>
 * <li>id:19 = “Ogrewatch Cut”,10</li>
 * <li>id:20 = “Veloka Slope”,10</li>
 * <li>id:21 = “Durios Gulch”,10</li>
 * <li>id:22 = “Bravost Escarpment”,10</li>
 * <li>id:23 = “Garrison”,25</li>
 * <li>id:24 = “Champion’s demense”,5</li>
 * <li>id:25 = “Redbriar”,10</li>
 * <li>id:26 = “Greenlake”,10</li>
 * <li>id:27 = “Ascension Bay”,25</li>
 * <li>id:28 = “Dawn’s Eyrie”,10</li>
 * <li>id:29 = “The Spiritholme”,5</li>
 * <li>id:30 = “Woodhaven”,10</li>
 * <li>id:31 = “Askalion Hills”,25</li>
 * <li>id:32 = “Etheron Hills”,25</li>
 * <li>id:33 = “Dreaming Bay”,25</li>
 * <li>id:34 = “Victors’s Lodge”,5</li>
 * <li>id:35 = “Greenbriar”,10</li>
 * <li>id:36 = “Bluelake”,10</li>
 * <li>id:37 = “Garrison”,25</li>
 * <li>id:38 = “Longview”,10</li>
 * <li>id:39 = “The Godsword”,5</li>
 * <li>id:40 = “Cliffside”,10</li>
 * <li>id:41 = “Shadaran Hills”,25</li>
 * <li>id:42 = “Redlake”,10</li>
 * <li>id:43 = “Hero’s Lodge”,5</li>
 * <li>id:44 = “Dreadfall Bay”,25</li>
 * <li>id:45 = “Bluebriar”,10</li>
 * <li>id:46 = “Garrison”,25</li>
 * <li>id:47 = “Sunnyhill”,10</li>
 * <li>id:48 = “Faithleap”,5</li>
 * <li>id:49 = “Bluevale Refuge”,5</li>
 * <li>id:50 = “Bluewater Lowlands”,5</li>
 * <li>id:51 = “Astralholme”,5</li>
 * <li>id:52 = “Arah’s Hope”,5</li>
 * <li>id:53 = “Greenvale Refuge”,5</li>
 * <li>id:54 = “Foghaven”,5</li>
 * <li>id:55 = “Redwater Lowlands”,5</li>
 * <li>id:56 = “The Titanpaw”,5</li>
 * <li>id:57 = “Cragtop”,10</li>
 * <li>id:58 = “Godslore”,5</li>
 * <li>id:59 = “Redvale Refuge”,5</li>
 * <li>id:60 = “Stargrove”,5</li>
 * <li>id:61 = “Greenwater Lowlands”,5];</li>
 * </ul>
 */
enum WVWLocationType implements IWVWLocationType {
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
	RED_WORLD_OVERLOOK(1, WVWMapType.CENTER),
	BLUE_WORLD_VALLEY(2, WVWMapType.CENTER),
	GREEN_WORLD_LOWLANDS(3, WVWMapType.CENTER),
	GOLANTA_CLEARING(4, WVWMapType.CENTER),
	PANGLOSS_RISE(5, WVWMapType.CENTER),
	SPELDAN_CLEARCUT(6, WVWMapType.CENTER),
	DANELON_PASSAGE(7, WVWMapType.CENTER),
	UMBERGLADE_WOODS(8, WVWMapType.CENTER),
	STONEMIST_CASTLE(9, WVWMapType.CENTER),
	ROGUES_QUARRY(10, WVWMapType.CENTER),
	ALDONS_LEDGE(11, WVWMapType.CENTER),
	WILDCREEK_RUN(12, WVWMapType.CENTER),
	JERRIFERS_SLOUGH(13, WVWMapType.CENTER),
	KLOVAN_GULLY(14, WVWMapType.CENTER),
	LANGOR_GULCH(15, WVWMapType.CENTER),
	QUENTIN_LAKE(16, WVWMapType.CENTER),
	MENDONS_GAP(17, WVWMapType.CENTER),
	ANZALIAS_PASS(18, WVWMapType.CENTER),
	OGRE_WATCH(19, WVWMapType.CENTER),
	VELOKA_SLOPE(20, WVWMapType.CENTER),
	DURIOS_GULCH(21, WVWMapType.CENTER),
	BRAVOST_ESCARPMENT(22, WVWMapType.CENTER),
	
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
	ETHERON_HILLS(32, WVWMapType.RED),
	DREAMING_BAY(33, WVWMapType.RED),
	VICTORS_LODGE(34, WVWMapType.RED),
	GREENBRIAR(35, WVWMapType.RED),
	BLUELAKE(36, WVWMapType.RED),
	RED_WORLD_GARRISON(37, WVWMapType.RED), // ???
	LONGVIEW(38, WVWMapType.RED),
	THE_GODSWORD(39, WVWMapType.RED),
	CLIFFSIDE(40, WVWMapType.RED),
	BLUEWATER_LOWLANDS(50, WVWMapType.RED),
	ASTRALHOLME(51, WVWMapType.RED),
	ARAHS_HOPE(52, WVWMapType.RED),
	GREENVALE_REFUGE(53, WVWMapType.RED),
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
	CHAMPIONS_DEMENSE(24, WVWMapType.BLUE),
	BLUE_WORLD_GARRISON(23, WVWMapType.BLUE), // ???
	REDBRIAR(25, WVWMapType.BLUE),
	GREENLAKE(26, WVWMapType.BLUE),
	ASCENSION_BAY(27, WVWMapType.BLUE),
	DAWNS_EYRIE(28, WVWMapType.BLUE),
	THE_SPIRITHOLME(29, WVWMapType.BLUE),
	WOODHAVEN(30, WVWMapType.BLUE),
	ASKALION_HILLS(31, WVWMapType.BLUE),
	GODSLORE(58, WVWMapType.BLUE),
	REDVALE_REFUGE(59, WVWMapType.BLUE),
	STARGROVE(60, WVWMapType.BLUE),
	GREEMWATER_LOWLANDS(61, WVWMapType.BLUE),
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
	SHADARAN_HILLS(41, WVWMapType.GREEN),
	REDLAKE(42, WVWMapType.GREEN),
	HEROS_LODGE(43, WVWMapType.GREEN),
	DREADFALL_BAY(44, WVWMapType.GREEN),
	BLUEBRIAR(45, WVWMapType.GREEN),
	GREEN_WORLD_GARRISON(46, WVWMapType.GREEN), // ???
	SUNNYHILL(47, WVWMapType.GREEN),
	FAITHLEAP(48, WVWMapType.GREEN),
	BLUEVALE_REFUGE(49, WVWMapType.GREEN),
	FOGHAVEN(54, WVWMapType.GREEN),
	REDWATER_LOWLANDS(55, WVWMapType.GREEN),
	THE_TITANPAW(56, WVWMapType.GREEN),
	CRAGTOP(57, WVWMapType.GREEN);

	private static final IWVWMapType CENTER_MAPTYPE = WVWMapType.CENTER;
	private static final IWVWMapType RED_MAPTYPE = WVWMapType.RED;
	private static final IWVWMapType GREEN_MAPTYPE = WVWMapType.GREEN;
	private static final IWVWMapType BLUE_MAPTYPE = WVWMapType.BLUE;
	
	private static final Logger LOGGER = Logger.getLogger(WVWLocationType.class);
	private static final Map<Integer, IWVWLocationType> LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID;
	private static final Map<IWVWMapType, Set<IWVWLocationType>> LOCATIONTYPES_MAPPED_BY_MAPTYPE;
	static {
		final ImmutableMap.Builder<Integer, IWVWLocationType> mapByObjectiveIdBuilder = ImmutableMap.builder();
		final ImmutableSet.Builder<IWVWLocationType> centerLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<IWVWLocationType> redLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<IWVWLocationType> greenLocationSetBuilder = ImmutableSet.builder();
		final ImmutableSet.Builder<IWVWLocationType> blueLocationSetBuilder = ImmutableSet.builder();
		for (WVWLocationType location : WVWLocationType.values()) {
			if (location.getObjectiveId().isPresent()) {
				mapByObjectiveIdBuilder.put(location.getObjectiveId().get(), location);
			}
			if(location.getMapType().isCenter()) {
				centerLocationSetBuilder.add(location);
			}else if(location.getMapType().isBlue()) {
				blueLocationSetBuilder.add(location);
			}else if(location.getMapType().isGreen()) {
				greenLocationSetBuilder.add(location);
			}else if(location.getMapType().isRed()) {
				redLocationSetBuilder.add(location);				
			}else {
				LOGGER.fatal("Invalid map type("+location.getMapType()+") for location "+location);
				throw new IllegalStateException("Invalid map type("+location.getMapType()+") for location "+location);
			}
		}
		LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID = mapByObjectiveIdBuilder.build();
		LOCATIONTYPES_MAPPED_BY_MAPTYPE = ImmutableMap.<IWVWMapType, Set<IWVWLocationType>> builder()
				.put(RED_MAPTYPE, redLocationSetBuilder.build())
				.put(GREEN_MAPTYPE, greenLocationSetBuilder.build())
				.put(BLUE_MAPTYPE, blueLocationSetBuilder.build())
				.put(CENTER_MAPTYPE, centerLocationSetBuilder.build()).build();
		
		LOGGER.trace("Initialized "+WVWLocationType.class.getSimpleName()+":\n"+LOCATIONTYPES_MAPPED_BY_MAPTYPE+"\n"+LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID);
		checkState(WVWLocationType.forMapTyp(CENTER_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(BLUE_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(GREEN_MAPTYPE).isPresent());
		checkState(WVWLocationType.forMapTyp(RED_MAPTYPE).isPresent());		
		checkState(WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size() == WVWLocationType.forMapTyp(RED_MAPTYPE).get().size(), "green("+WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size()+")==red("+WVWLocationType.forMapTyp(RED_MAPTYPE).get().size()+")");
		checkState(WVWLocationType.forMapTyp(BLUE_MAPTYPE).get().size() == WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size(), "blue("+WVWLocationType.forMapTyp(BLUE_MAPTYPE).get().size()+")==green("+WVWLocationType.forMapTyp(GREEN_MAPTYPE).get().size()+")");			
	}

	private final IWVWMapType mapType;
	private final Optional<Integer> objectiveId;

	private WVWLocationType(IWVWMapType type) {
		this(null, checkNotNull(type));
	}

	private WVWLocationType(Integer objectiveId, IWVWMapType type) {
		this.objectiveId = Optional.fromNullable(objectiveId);
		checkNotNull(type);
		this.mapType = type;
	}

	public String getLabel() {
		return this.name();
	}

	public Optional<Integer> getObjectiveId() {
		return this.objectiveId;
	}

	public static Optional<IWVWLocationType> forObjectiveId(int objectiveId) {
		return Optional.fromNullable(LOCATIONTYPES_MAPPED_BY_OBJECTIVE_ID.get(objectiveId));
	}

	public static Optional<Set<IWVWLocationType>> forMapTyp(IWVWMapType mapType) {
		return Optional.fromNullable(LOCATIONTYPES_MAPPED_BY_MAPTYPE.get(mapType));
	}

	public IWVWMapType getMapType() {
		return this.mapType;
	}
}