package yagw2api.explorer.rcp.wvw;

import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWorld;

class OwningWorldMatchingObjectiveColumnLabelProvider extends TypeSafeColumnLabelProvider<IWVWObjective> {
	public OwningWorldMatchingObjectiveColumnLabelProvider() {
		super(IWVWObjective.class);
	}

	@Override
	protected Color getTypeSafeBackground(final IWVWObjective element) {
		if (element.getOwner().isPresent() && element.getMap().isPresent() && element.getMap().get().getMatch().isPresent()) {
			final IWorld world = element.getOwner().get();
			final IWVWMatch match = element.getMap().get().getMatch().get();
			if (world.equals(match.getRedWorld())) {
				return SWTResourceManager.getColor(WVWUIConstants.RGB_RED_WORLD_BG);
			} else if (world.equals(match.getGreenWorld())) {
				return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_BG);
			} else if (world.equals(match.getBlueWorld())) {
				return SWTResourceManager.getColor(WVWUIConstants.RGB_BLUE_WORLD_BG);
			} else {
				return super.getTypeSafeBackground(element);
			}
		} else {
			return super.getTypeSafeBackground(element);
		}
	}

	@Override
	protected Color getTypeSafeForeground(final IWVWObjective element) {
		if (element.getOwner().isPresent() && element.getMap().isPresent() && element.getMap().get().getMatch().isPresent()) {
			final IWorld world = element.getOwner().get();
			final IWVWMatch match = element.getMap().get().getMatch().get();
			if (world.equals(match.getRedWorld())) {
				return SWTResourceManager.getColor(WVWUIConstants.RGB_RED_WORLD_FG);
			} else if (world.equals(match.getGreenWorld())) {
				return SWTResourceManager.getColor(WVWUIConstants.RGB_GREEN_WORLD_FG);
			} else if (world.equals(match.getBlueWorld())) {
				return SWTResourceManager.getColor(WVWUIConstants.RGB_BLUE_WORLD_FG);
			} else {
				return super.getTypeSafeForeground(element);
			}
		} else {
			return super.getTypeSafeForeground(element);
		}
	}
}