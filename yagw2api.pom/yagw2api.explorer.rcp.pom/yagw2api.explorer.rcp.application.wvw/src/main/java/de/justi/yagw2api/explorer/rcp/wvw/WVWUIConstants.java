package de.justi.yagw2api.explorer.rcp.wvw;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * yagw2api.explorer.rcp.application.wvw
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.function.Function;

import org.eclipse.swt.graphics.RGB;

public final class WVWUIConstants {
	public static final RGB RGB_RED_WORLD_FG = new RGB(175, 25, 10);
	public static final RGB RGB_RED_WORLD_BG = new RGB(255, 255, 255);
	public static final RGB RGB_GREEN_WORLD_FG = new RGB(70, 152, 42);
	public static final RGB RGB_GREEN_WORLD_BG = new RGB(255, 255, 255);
	public static final RGB RGB_BLUE_WORLD_FG = new RGB(35, 129, 199);
	public static final RGB RGB_BLUE_WORLD_BG = new RGB(255, 255, 255);
	public static final String LABEL_NO_SUCH_DATA = "no such data";
	public static final NumberFormat NUMBER_FORMAT_POINTS = new DecimalFormat("###,###,##0");

	public static final Function<Duration, String> DURATION_FORMAT = duration -> {
		Duration rest = duration;
		final long days = rest.toDays();
		rest = duration.minusDays(days);
		final long hours = rest.toHours();
		rest = duration.minusHours(hours);
		final long minutes = rest.toMinutes();
		rest = duration.minusMinutes(minutes);
		final long seconds = rest.getSeconds();

		final StringBuilder builder = new StringBuilder();
		if (days > 0) {
			builder.append(days);
			builder.append("d");
		}
		if (hours > 0) {
			builder.append(hours);
			builder.append("h");
		}
		if (minutes > 0) {
			builder.append(minutes);
			builder.append("m");
		}
		if (seconds > 0) {
			builder.append(seconds);
			builder.append("s");
		}
		if (days + hours + minutes + seconds <= 0) {
			builder.append("0s");
		}
		return builder.toString();
	};

	private WVWUIConstants() {

		throw new AssertionError("no instance");
	}
}
