package yagw2api.explorer.rcp.wvw;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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

	private WVWUIConstants() {
		throw new AssertionError("no instance");
	}
}
