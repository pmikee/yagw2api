package de.justi.yagw2api.arenanet.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import com.google.common.base.Optional;

import de.justi.yagw2api.arenanet.DTOConstants;
import de.justi.yagw2api.arenanet.IWVWMapDTO;
import de.justi.yagw2api.arenanet.IWVWMatchDTO;
import de.justi.yagw2api.arenanet.IWVWMatchDetailsDTO;
import de.justi.yagw2api.arenanet.IWVWMatchesDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveDTO;
import de.justi.yagw2api.arenanet.IWVWObjectiveNameDTO;
import de.justi.yagw2api.test.AbstractYAGW2APITest;

public final class WVWServiceTest extends AbstractYAGW2APITest {
	private static final int NUMBER_OF_MAPS = 4;
	private static final String EXISTING_MATCH_ID_4_TEST = "1-1";
	private static final int NUMBER_OF_OBJECTIVE_NAMES = 61;

	@Test
	public void testZuluTimeFormat() throws ParseException {
		final Calendar cal = Calendar.getInstance(Locale.US);
		cal.clear();
		cal.set(2013, 5, 8);
		cal.set(Calendar.HOUR, 3);
		Date result = ServiceUtils.ZULU_DATE_FORMAT.parse("2013-06-08T01:00:00Z");
		assertEquals(ServiceUtils.ZULU_DATE_FORMAT.format(cal.getTime()) + " != " + ServiceUtils.ZULU_DATE_FORMAT.format(result), cal.getTime(), result);

		cal.clear();
		cal.set(2013, 5, 14);
		cal.set(Calendar.HOUR, 20);
		result = ServiceUtils.ZULU_DATE_FORMAT.parse("2013-06-14T18:00:00Z");
		assertEquals(ServiceUtils.ZULU_DATE_FORMAT.format(cal.getTime()) + " != " + ServiceUtils.ZULU_DATE_FORMAT.format(result), cal.getTime(), result);
	}

	@Test
	public void testRetrieveAllMatches() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);

		final IWVWMatchesDTO result = service.retrieveAllMatches();
		assertNotNull(result);
		assertEquals(17, result.getMatches().length);

		for (IWVWMatchDTO match : result.getMatches()) {
			assertTrue(match.getBlueWorldId() > 0);
			assertTrue(match.getRedWorldId() > 0);
			assertTrue(match.getGreenWorldId() > 0);
			assertNotNull(match.getStartTime());
			assertNotNull(match.getEndTime());
			assertNotNull(match.getId());

			assertNotNull(match.getBlueWorldName(Locale.GERMAN));
			assertTrue(match.getBlueWorldName(Locale.GERMAN).isPresent());
			assertNotNull(match.getBlueWorldName(Locale.GERMAN).get());
			assertNotNull(match.getBlueWorldName(Locale.GERMANY));
			assertTrue(match.getBlueWorldName(Locale.GERMANY).isPresent());
			assertNotNull(match.getBlueWorldName(Locale.GERMANY).get());

			assertNotNull(match.getGreenWorldName(Locale.GERMAN));
			assertTrue(match.getGreenWorldName(Locale.GERMAN).isPresent());
			assertNotNull(match.getGreenWorldName(Locale.GERMAN).get());
			assertNotNull(match.getGreenWorldName(Locale.GERMANY));
			assertTrue(match.getGreenWorldName(Locale.GERMANY).isPresent());
			assertNotNull(match.getGreenWorldName(Locale.GERMANY).get());

			assertNotNull(match.getRedWorldName(Locale.GERMAN));
			assertTrue(match.getRedWorldName(Locale.GERMAN).isPresent());
			assertNotNull(match.getRedWorldName(Locale.GERMAN).get());
			assertNotNull(match.getRedWorldName(Locale.GERMANY));
			assertTrue(match.getRedWorldName(Locale.GERMANY).isPresent());
			assertNotNull(match.getRedWorldName(Locale.GERMANY).get());

			assertNotNull(match.getDetails());
			assertTrue(match.getDetails().isPresent());
			assertNotNull(match.getDetails().get());
			assertNotNull(match.getDetails().get().getBlueMap());
			assertEquals(DTOConstants.BLUE_MAP_TYPE_STRING, match.getDetails().get().getBlueMap().getType());
			assertNotNull(match.getDetails().get().getRedMap());
			assertEquals(DTOConstants.RED_MAP_TYPE_STRING, match.getDetails().get().getRedMap().getType());
			assertNotNull(match.getDetails().get().getGreenMap());
			assertEquals(DTOConstants.GREEN_MAP_TYPE_STRING, match.getDetails().get().getGreenMap().getType());
			assertNotNull(match.getDetails().get().getCenterMap());
			assertEquals(DTOConstants.CENTER_MAP_TYPE_STRING, match.getDetails().get().getCenterMap().getType());
			assertEquals(match.getId(), match.getDetails().get().getMatchID());

			assertTrue(match.getDetails().get().getBlueScore() >= 0);
			assertTrue(match.getDetails().get().getRedScore() >= 0);
			assertTrue(match.getDetails().get().getGreenScore() >= 0);

			assertNotNull(match.getDetails().get().getMatch());
			assertTrue(match.getDetails().get().getMatch().isPresent());
			assertNotNull(match.getDetails().get().getMatch().get());
			assertEquals(match, match.getDetails().get().getMatch().get());

			for (IWVWMapDTO map : match.getDetails().get().getMaps()) {
				assertTrue(map.getBlueScore() >= 0);
				assertTrue(map.getRedScore() >= 0);
				assertTrue(map.getGreenScore() >= 0);
				assertNotNull(map.getType());
				assertTrue(map.getType().trim().length() > 0);
				assertNotNull(map.getObjectives());
				assertTrue(map.getObjectives().length > 0);

				for (IWVWObjectiveDTO objective : map.getObjectives()) {
					assertTrue("Guild ID of " + objective + " should either be null or a string without leading or tailing whitespaces.", (objective.getGuildId() == null)
							|| ((objective.getGuildId().trim().length() > 0) && (objective.getGuildId().length() == objective.getGuildId().trim().length())));
					assertTrue(objective.getId() > 0);
					assertTrue("Owner of " + objective + " should either be null or equal to one of the following strings: " + DTOConstants.OWNER_BLUE_STRING + ", " + DTOConstants.OWNER_GREEN_STRING
							+ ", " + DTOConstants.OWNER_RED_STRING,
							(objective.getOwner() == null) || objective.getOwner().equals(DTOConstants.OWNER_BLUE_STRING) || objective.getOwner().equals(DTOConstants.OWNER_GREEN_STRING)
									|| objective.getOwner().equals(DTOConstants.OWNER_RED_STRING));
					assertNotNull(objective.getGuildDetails());
					if (objective.getGuildDetails().isPresent()) {
						assertNotNull(objective.getGuildDetails().get());
						assertEquals(objective.getGuildId(), objective.getGuildDetails().get().getId());
						assertNotNull(objective.getGuildDetails().get().getName());
						assertNotNull(objective.getGuildDetails().get().getTag());
						assertNotNull(objective.getGuildDetails().get().getEmblem());
						if (objective.getGuildDetails().get().getEmblem().isPresent()) {
							assertTrue(objective.getGuildDetails().get().getEmblem().get().getBackgroundColorId() > 0);
							assertTrue(objective.getGuildDetails().get().getEmblem().get().getBackgroundId() > 0);
							assertTrue(objective.getGuildDetails().get().getEmblem().get().getForegroundId() > 0);
							assertTrue(objective.getGuildDetails().get().getEmblem().get().getForegroundPrimaryColorId() > 0);
							assertTrue(objective.getGuildDetails().get().getEmblem().get().getForegroundSecondaryColorId() > 0);
							assertNotNull(objective.getGuildDetails().get().getEmblem().get().getFlags());
						}
					}
				}
			}
		}
	}

	@Test
	public void testRetrieveAllObjectiveNames() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);
		final IWVWObjectiveNameDTO[] resultA = service.retrieveAllObjectiveNames(Locale.GERMANY);
		assertNotNull(resultA);
		final IWVWObjectiveNameDTO[] resultB = service.retrieveAllObjectiveNames(Locale.GERMAN);
		assertNotNull(resultB);

		assertEquals(NUMBER_OF_OBJECTIVE_NAMES, resultA.length);
		assertEquals(NUMBER_OF_OBJECTIVE_NAMES, resultB.length);
		assertTrue(Arrays.deepEquals(resultA, resultB));

		for (IWVWObjectiveNameDTO name : resultA) {
			assertTrue(name.getId() > 0);
			assertNotNull(name.getName());
			assertTrue(name.getName().trim().length() > 0);
		}
	}

	@Test
	public void testRetrieveMatch() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);

		final Optional<IWVWMatchDTO> result = service.retrieveMatch(EXISTING_MATCH_ID_4_TEST);
		assertNotNull(result);
		assertTrue(result.isPresent());
		assertNotNull(result.get());
		assertTrue(result.get().getBlueWorldId() > 0);
		assertTrue(result.get().getRedWorldId() > 0);
		assertTrue(result.get().getGreenWorldId() > 0);
		assertNotNull(result.get().getStartTime());
		assertNotNull(result.get().getEndTime());
		assertNotNull(result.get().getId());
	}

	@Test
	public void testRetrieveMatchDetails() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);

		final Optional<IWVWMatchDetailsDTO> result = service.retrieveMatchDetails(EXISTING_MATCH_ID_4_TEST);
		assertNotNull(result);
		assertTrue(result.isPresent());
		assertNotNull(result.get());

		assertNotNull(result.get().getBlueMap());
		assertEquals(DTOConstants.BLUE_MAP_TYPE_STRING, result.get().getBlueMap().getType());
		assertNotNull(result.get().getRedMap());
		assertEquals(DTOConstants.RED_MAP_TYPE_STRING, result.get().getRedMap().getType());
		assertNotNull(result.get().getGreenMap());
		assertEquals(DTOConstants.GREEN_MAP_TYPE_STRING, result.get().getGreenMap().getType());
		assertNotNull(result.get().getCenterMap());
		assertEquals(DTOConstants.CENTER_MAP_TYPE_STRING, result.get().getCenterMap().getType());

		assertTrue(result.get().getBlueScore() >= 0);
		assertTrue(result.get().getRedScore() >= 0);
		assertTrue(result.get().getGreenScore() >= 0);

		assertEquals(NUMBER_OF_MAPS, result.get().getMaps().length);
		assertEquals(EXISTING_MATCH_ID_4_TEST, result.get().getMatchID());
		assertNotNull(result.get().getMatch());
		assertTrue(result.get().getMatch().isPresent());
		assertNotNull(result.get().getMatch().get());
		assertEquals(result.get(), result.get().getMatch().get().getDetails().get());

		for (IWVWMapDTO map : result.get().getMaps()) {
			assertTrue(map.getBlueScore() >= 0);
			assertTrue(map.getRedScore() >= 0);
			assertTrue(map.getGreenScore() >= 0);
			assertNotNull(map.getType());
			assertTrue(map.getType().trim().length() > 0);
			assertNotNull(map.getObjectives());
			assertTrue(map.getObjectives().length > 0);

			for (IWVWObjectiveDTO objective : map.getObjectives()) {
				assertTrue("Guild ID of " + objective + " should either be null or a string without leading or tailing whitespaces.", (objective.getGuildId() == null)
						|| ((objective.getGuildId().trim().length() > 0) && (objective.getGuildId().length() == objective.getGuildId().trim().length())));
				assertTrue(objective.getId() > 0);
				assertTrue("Owner of " + objective + " should either be null or equal to one of the following strings: " + DTOConstants.OWNER_BLUE_STRING + ", " + DTOConstants.OWNER_GREEN_STRING
						+ ", " + DTOConstants.OWNER_RED_STRING,
						(objective.getOwner() == null) || objective.getOwner().equals(DTOConstants.OWNER_BLUE_STRING) || objective.getOwner().equals(DTOConstants.OWNER_GREEN_STRING)
								|| objective.getOwner().equals(DTOConstants.OWNER_RED_STRING));
				assertNotNull(objective.getGuildDetails());
				if (objective.getGuildDetails().isPresent()) {
					assertNotNull(objective.getGuildDetails().get());
					assertEquals(objective.getGuildId(), objective.getGuildDetails().get().getId());
					assertNotNull(objective.getGuildDetails().get().getName());
					assertNotNull(objective.getGuildDetails().get().getTag());
					assertNotNull(objective.getGuildDetails().get().getEmblem());
					if (objective.getGuildDetails().get().getEmblem().isPresent()) {
						assertTrue(objective.getGuildDetails().get().getEmblem().get().getBackgroundColorId() > 0);
						assertTrue(objective.getGuildDetails().get().getEmblem().get().getBackgroundId() > 0);
						assertTrue(objective.getGuildDetails().get().getEmblem().get().getForegroundId() > 0);
						assertTrue(objective.getGuildDetails().get().getEmblem().get().getForegroundPrimaryColorId() > 0);
						assertTrue(objective.getGuildDetails().get().getEmblem().get().getForegroundSecondaryColorId() > 0);
						assertNotNull(objective.getGuildDetails().get().getEmblem().get().getFlags());
					}
				}
			}
		}
	}

	@Test
	public void testRetrieveObjectiveName() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);

		Optional<IWVWObjectiveNameDTO> nameA;
		Optional<IWVWObjectiveNameDTO> nameB;
		for (int id = 1; id <= NUMBER_OF_OBJECTIVE_NAMES; id++) {
			nameA = service.retrieveObjectiveName(Locale.GERMAN, id);
			assertNotNull(nameA);
			assertTrue(nameA.isPresent());

			nameB = service.retrieveObjectiveName(Locale.GERMANY, id);
			assertNotNull(nameB);
			assertTrue(nameB.isPresent());

			assertEquals(nameA.get(), nameB.get());
			assertEquals(id, nameA.get().getId());
			assertNotNull(nameA.get().getName());
			assertTrue(nameA.get().getName().trim().length() > 0);
		}
	}
}
