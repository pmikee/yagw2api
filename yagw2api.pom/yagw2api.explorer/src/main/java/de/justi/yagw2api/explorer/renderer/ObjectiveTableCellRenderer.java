package de.justi.yagw2api.explorer.renderer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-Application
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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.google.common.base.Optional;

import de.justi.yagw2api.explorer.model.MapObjectivesTableModel;
import de.justi.yagw2api.explorer.view.MainWindow;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWorld;

public final class ObjectiveTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = -8506469778783597659L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		checkState(table.getModel() instanceof MapObjectivesTableModel);
		final MapObjectivesTableModel model = (MapObjectivesTableModel) table.getModel();
		final Optional<IWVWObjective> objective = model.getObjectiveForRow(table.convertRowIndexToModel(row));
		checkArgument(objective.isPresent());
		final Optional<IWorld> owner = objective.get().getOwner();
		final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		final Optional<IWVWMap> map = objective.get().getMap();
		if (map.isPresent()) {
			final Optional<IWVWMatch> match = map.get().getMatch();
			if (match.isPresent()) {
				if(!owner.isPresent()){
					c.setBackground(isSelected ? MainWindow.NEUTRAL_FG : MainWindow.NEUTRAL_BG);
				}else{
					if (match.get().getBlueWorld().equals(owner.get())) {
						c.setBackground(isSelected ? MainWindow.BLUE_WORLD_FG : MainWindow.BLUE_WORLD_BG);
					} else if (match.get().getGreenWorld().equals(owner.get())) {
						c.setBackground(isSelected ? MainWindow.GREEN_WORLD_FG : MainWindow.GREEN_WORLD_BG);
					} else if (match.get().getRedWorld().equals(owner.get())) {
						c.setBackground(isSelected ? MainWindow.RED_WORLD_FG : MainWindow.RED_WORLD_BG);
					}
				}
			}
		}
		return c;
	}
}
