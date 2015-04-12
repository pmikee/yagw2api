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
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import de.justi.yagw2api.explorer.rcp.swt.TypeSafeColumnLabelProvider;
import de.justi.yagw2api.wrapper.domain.world.IWorld;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWMatch;
import de.justi.yagw2api.wrapper.domain.wvw.IWVWObjective;

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
