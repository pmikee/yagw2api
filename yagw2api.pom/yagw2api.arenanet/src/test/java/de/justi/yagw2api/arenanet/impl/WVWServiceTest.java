package de.justi.yagw2api.arenanet.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Arenanet
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */

import static com.google.common.base.Preconditions.checkNotNull;
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
	private static final Locale SUPPORTED_LOCALE_A1 = Locale.GERMAN;
	private static final Locale SUPPORTED_LOCALE_A2 = Locale.GERMANY;
	private static final int NUMBER_OF_MAPS = 4;
	private static final String EXISTING_MATCH_ID_4_TEST = "1-1";
	private static final int NUMBER_OF_OBJECTIVE_NAMES = 76;

	private static final void assertConsistencyOfMapArray(IWVWMapDTO[] maps) {
		checkNotNull(maps);
		for (IWVWMapDTO map : maps) {
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

	private static final void assertConsistencyOfMapTypsForMatchDetails(IWVWMatchDetailsDTO matchDetails) {
		checkNotNull(matchDetails);
		assertNotNull(matchDetails.getBlueMap());
		assertEquals(DTOConstants.BLUE_MAP_TYPE_STRING, matchDetails.getBlueMap().getType());
		assertNotNull(matchDetails.getRedMap());
		assertEquals(DTOConstants.RED_MAP_TYPE_STRING, matchDetails.getRedMap().getType());
		assertNotNull(matchDetails.getGreenMap());
		assertEquals(DTOConstants.GREEN_MAP_TYPE_STRING, matchDetails.getGreenMap().getType());
		assertNotNull(matchDetails.getCenterMap());
		assertEquals(DTOConstants.CENTER_MAP_TYPE_STRING, matchDetails.getCenterMap().getType());
	}

	private static final void assertConsistencyOfMatch(IWVWMatchDTO match) {
		checkNotNull(match);
		assertNotNull(match.getDetails());
		assertTrue(match.getDetails().isPresent());
		assertNotNull(match.getDetails().get());
		assertEquals(match.getId(), match.getDetails().get().getMatchID());

		assertTrue(match.getBlueWorldId() > 0);
		assertTrue(match.getRedWorldId() > 0);
		assertTrue(match.getGreenWorldId() > 0);
		assertNotNull(match.getStartTime());
		assertNotNull(match.getEndTime());
		assertNotNull(match.getId());

		assertConsistencyOfMatchDetails(match.getDetails().get());
	}

	private static final void assertConsistencyOfMatchDetails(IWVWMatchDetailsDTO matchDetails) {
		checkNotNull(matchDetails);
		assertNotNull(matchDetails.getMatch());
		assertTrue(matchDetails.getMatch().isPresent());
		assertNotNull(matchDetails.getMatch().get());
		assertEquals(matchDetails.getMatchID(), matchDetails.getMatch().get().getId());

		assertTrue(matchDetails.getBlueScore() >= 0);
		assertTrue(matchDetails.getRedScore() >= 0);
		assertTrue(matchDetails.getGreenScore() >= 0);

		assertEquals(NUMBER_OF_MAPS, matchDetails.getMaps().length);

		assertConsistencyOfMapTypsForMatchDetails(matchDetails);
		assertConsistencyOfMapArray(matchDetails.getMaps());
	}

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

			assertNotNull(match.getBlueWorldName(SUPPORTED_LOCALE_A1));
			assertTrue(match.getBlueWorldName(SUPPORTED_LOCALE_A1).isPresent());
			assertNotNull(match.getBlueWorldName(SUPPORTED_LOCALE_A1).get());
			assertNotNull(match.getBlueWorldName(SUPPORTED_LOCALE_A2));
			assertTrue(match.getBlueWorldName(SUPPORTED_LOCALE_A2).isPresent());
			assertNotNull(match.getBlueWorldName(SUPPORTED_LOCALE_A2).get());

			assertNotNull(match.getGreenWorldName(SUPPORTED_LOCALE_A1));
			assertTrue(match.getGreenWorldName(SUPPORTED_LOCALE_A1).isPresent());
			assertNotNull(match.getGreenWorldName(SUPPORTED_LOCALE_A1).get());
			assertNotNull(match.getGreenWorldName(SUPPORTED_LOCALE_A2));
			assertTrue(match.getGreenWorldName(SUPPORTED_LOCALE_A2).isPresent());
			assertNotNull(match.getGreenWorldName(SUPPORTED_LOCALE_A2).get());

			assertNotNull(match.getRedWorldName(SUPPORTED_LOCALE_A1));
			assertTrue(match.getRedWorldName(SUPPORTED_LOCALE_A1).isPresent());
			assertNotNull(match.getRedWorldName(SUPPORTED_LOCALE_A1).get());
			assertNotNull(match.getRedWorldName(SUPPORTED_LOCALE_A2));
			assertTrue(match.getRedWorldName(SUPPORTED_LOCALE_A2).isPresent());
			assertNotNull(match.getRedWorldName(SUPPORTED_LOCALE_A2).get());

			assertConsistencyOfMatch(match);
		}
	}

	@Test
	public void testRetrieveAllObjectiveNames() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);
		final IWVWObjectiveNameDTO[] resultA = service.retrieveAllObjectiveNames(SUPPORTED_LOCALE_A1);
		assertNotNull(resultA);
		final IWVWObjectiveNameDTO[] resultB = service.retrieveAllObjectiveNames(SUPPORTED_LOCALE_A2);
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

		assertConsistencyOfMatch(result.get());
	}

	@Test
	public void testRetrieveMatchDetails() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);

		final Optional<IWVWMatchDetailsDTO> result = service.retrieveMatchDetails(EXISTING_MATCH_ID_4_TEST);
		assertNotNull(result);
		assertTrue(result.isPresent());
		assertEquals(EXISTING_MATCH_ID_4_TEST, result.get().getMatchID());

		assertConsistencyOfMatchDetails(result.get());
	}

	@Test
	public void testRetrieveObjectiveName() {
		final WVWDTOFactory dtoFactory = new WVWDTOFactory();
		final WVWService service = new WVWService(dtoFactory);

		Optional<IWVWObjectiveNameDTO> nameA;
		Optional<IWVWObjectiveNameDTO> nameB;
		for (int id = 1; id <= NUMBER_OF_OBJECTIVE_NAMES; id++) {
			nameA = service.retrieveObjectiveName(SUPPORTED_LOCALE_A1, id);
			assertNotNull(nameA);
			assertTrue(nameA.isPresent());

			nameB = service.retrieveObjectiveName(SUPPORTED_LOCALE_A2, id);
			assertNotNull(nameB);
			assertTrue(nameB.isPresent());

			assertEquals(nameA.get(), nameB.get());
			assertEquals(id, nameA.get().getId());
			assertNotNull(nameA.get().getName());
			assertTrue(nameA.get().getName().trim().length() > 0);
		}
	}
}
