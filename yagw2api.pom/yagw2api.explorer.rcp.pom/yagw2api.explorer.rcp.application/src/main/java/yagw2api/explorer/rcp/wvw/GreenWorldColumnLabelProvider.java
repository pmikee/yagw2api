package yagw2api.explorer.rcp.wvw;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;

class GreenWorldColumnLabelProvider<T> extends TypeSafeColumnLabelProvider<T> {
	public GreenWorldColumnLabelProvider(final Class<T> dataTypeClass) {
		super(dataTypeClass);
	}

	@Override
	protected Color getTypeSafeBackground(final T element) {
		return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_BG);
	}

	@Override
	protected Color getTypeSafeForeground(final T element) {
		return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_FG);
	}
}