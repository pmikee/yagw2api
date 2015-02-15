package de.justi.yagw2api.explorer.renderer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-Application
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import static com.google.common.base.Preconditions.checkState;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.justi.yagw2api.explorer.model.MatchDetailsTableModel;
import de.justi.yagw2api.explorer.view.MainWindow;

public final class MatchDetailsTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = -7631435914940282639L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		checkState(table.getModel() instanceof MatchDetailsTableModel);
		final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		c.setBackground(Color.WHITE);
		c.setForeground(Color.BLACK);
		if ((column > 0) && (column < 3)) {
			c.setForeground(row == 4 ? Color.WHITE : Color.BLACK);
			c.setBackground(row == 4 ? MainWindow.GREEN_WORLD_FG : MainWindow.GREEN_WORLD_BG);
		} else if ((column >= 3) && (column < 5)) {
			c.setForeground(row == 4 ? Color.WHITE : Color.BLACK);
			c.setBackground(row == 4 ? MainWindow.BLUE_WORLD_FG : MainWindow.BLUE_WORLD_BG);
		} else if ((column >= 5) && (column < 7)) {
			c.setForeground(row == 4 ? Color.WHITE : Color.BLACK);
			c.setBackground(row == 4 ? MainWindow.RED_WORLD_FG : MainWindow.RED_WORLD_BG);
		} else {
			c.setBackground(row == 4 ? Color.GRAY : Color.WHITE);
			if (row == 0) {
				c.setForeground(MainWindow.ETERNAL_BATTLEGROUNDS_FG);
			} else if (row == 1) {
				c.setForeground(MainWindow.GREEN_WORLD_FG);
			} else if (row == 2) {
				c.setForeground(MainWindow.BLUE_WORLD_FG);
			} else if (row == 3) {
				c.setForeground(MainWindow.RED_WORLD_FG);
			} else if (row == 4) {
				c.setForeground(Color.WHITE);
			}
		}

		return c;
	}
}
