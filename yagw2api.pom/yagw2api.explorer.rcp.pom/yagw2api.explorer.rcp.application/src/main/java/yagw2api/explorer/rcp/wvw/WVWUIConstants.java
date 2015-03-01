package yagw2api.explorer.rcp.wvw;

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
