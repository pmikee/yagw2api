package de.justi.yagw2api.core.arenanet.service.impl;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

import de.justi.yagw2api.core.YAGW2APICore;

public class WVWServiceTest {
	@Test
	public void testZuluTimeFormat() throws ParseException {
		final WVWService service = YAGW2APICore.getInjector().getInstance(WVWService.class);
		final DateFormat df = service.getZuluDateFormat();

		final Calendar cal = Calendar.getInstance(Locale.US);
		cal.clear();
		cal.set(2013, 5, 8);
		cal.set(Calendar.HOUR, 3);
		Date result = df.parse("2013-06-08T01:00:00Z");
		assertEquals(df.format(cal.getTime())+" != "+df.format(result),cal.getTime(), result);
		
		cal.clear();
		cal.set(2013, 5, 14);
		cal.set(Calendar.HOUR, 20);
		result = df.parse("2013-06-14T18:00:00Z");
		assertEquals(df.format(cal.getTime())+" != "+df.format(result),cal.getTime(), result);
	}
}
