package yagw2api.explorer.rcp.wvw;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import de.justi.yagw2api.wrapper.IWVWMatch;

class GreenWorldColumnLabelProvider extends TypeSafeColumnLabelProvider<IWVWMatch> {
	public GreenWorldColumnLabelProvider() {
		super(IWVWMatch.class);
	}

	@Override
	protected Color getTypeSafeBackground(final IWVWMatch element) {
		return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_BG);
	}

	@Override
	protected Color getTypeSafeForeground(final IWVWMatch element) {
		return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_FG);
	}
}