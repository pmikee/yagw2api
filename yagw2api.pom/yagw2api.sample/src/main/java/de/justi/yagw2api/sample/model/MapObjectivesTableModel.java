package de.justi.yagw2api.sample.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.table.AbstractTableModel;

import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public class MapObjectivesTableModel extends AbstractTableModel implements IWVWMapListener{
	private static final long serialVersionUID = -4657108157862724940L;
	
	private List<IWVWObjective> content = new CopyOnWriteArrayList<IWVWObjective>();
	
	public void wireUp(IWVWWrapper wrapper, IWVWMap map) {
		this.content.clear();
		this.content.addAll(map.getObjectives());
		this.fireTableDataChanged();
		wrapper.registerWVWMapListener(map, this);
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return this.content.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
			case 0:
				return this.content.get(rowIndex).getLabel().get();
			case 1:
				return this.content.get(rowIndex).getOwner().get().getName().get();
			default:
				throw new IllegalArgumentException("Unknown column: "+columnIndex);
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
