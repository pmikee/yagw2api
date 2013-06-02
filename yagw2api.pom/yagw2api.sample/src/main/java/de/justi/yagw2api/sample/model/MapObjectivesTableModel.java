package de.justi.yagw2api.sample.model;

import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public class MapObjectivesTableModel extends AbstractTableModel implements IWVWMapListener {
	private static final long serialVersionUID = -4657108157862724940L;
	
	final DateFormat DF = DateFormat.getTimeInstance(DateFormat.LONG);

	private List<IWVWObjective> content = new CopyOnWriteArrayList<IWVWObjective>();

	public void wireUp(IWVWWrapper wrapper, IWVWMap map) {
		this.content.clear();
		this.content.addAll(map.getObjectives());
		this.fireTableDataChanged();
		wrapper.unregisterWVWMapListener(this);
		wrapper.registerWVWMapListener(map, this);
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return this.content.size() + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == 0) {
			return columnIndex == 0 ? "Objekt" : columnIndex == 1 ? "Objekttyp" : columnIndex == 2 ? "Besitzer" : "Buff";
		} else {
			switch (columnIndex) {
			case 0:
				return this.content.get(rowIndex - 1).getLabel().get();
			case 1:
				return this.content.get(rowIndex - 1).getType().getLabel();
			case 2:
				return this.content.get(rowIndex - 1).getOwner().get().getName().get();
			case 3:
				return DF.format(this.content.get(rowIndex - 1).getEndOfBuffTimestamp().get().getTime());
			default:
				throw new IllegalArgumentException("Unknown column: " + columnIndex);
			}
		}

	}

	@Override
	public void notifyAboutChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
	}

	@Override
	public void notifyAboutObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		this.fireTableCellUpdated(this.content.indexOf(event.getObjective()), 1);
	}

	@Override
	public void notifyAboutObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		this.fireTableCellUpdated(this.content.indexOf(event.getObjective()), 0);
	}

}
