package de.justi.yagw2api.sample.view;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.IWorld;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.sample.model.MapObjectivesTableModel;

public class ObjectiveTableCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = -8506469778783597659L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		checkState(table.getModel() instanceof MapObjectivesTableModel);
		final MapObjectivesTableModel model = (MapObjectivesTableModel) table.getModel();
		final Optional<IWVWObjective> objective = model.getObjectiveForRow(table.convertRowIndexToModel(row));
		checkArgument(objective.isPresent());
		final Optional<IWorld> owner = objective.get().getOwner();
		checkArgument(owner.isPresent());
		final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		final Optional<IWVWMap> map = objective.get().getMap();
		if (map.isPresent()) {
			final Optional<IWVWMatch> match = map.get().getMatch();
			if (match.isPresent()) {
				if (match.get().getBlueWorld().equals(owner.get())) {
					c.setBackground(MainWindow.BLUE_WORLD_BG);
				} else if (match.get().getGreenWorld().equals(owner.get())) {
					c.setBackground(MainWindow.GREEN_WORLD_BG);
				} else if (match.get().getRedWorld().equals(owner.get())) {
					c.setBackground(MainWindow.RED_WORLD_BG);
				}
			}
		}
		return c;
	}
}