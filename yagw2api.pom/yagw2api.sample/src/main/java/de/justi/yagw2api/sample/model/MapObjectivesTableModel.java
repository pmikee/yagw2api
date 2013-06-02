package de.justi.yagw2api.sample.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;

import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public class MapObjectivesTableModel extends AbstractTableModel implements IWVWMapListener {
	private static final long	serialVersionUID	= -4657108157862724940L;

	final DateFormat			DF					= DateFormat.getTimeInstance(DateFormat.LONG);

	private List<IWVWObjective>	content				= new CopyOnWriteArrayList<IWVWObjective>();
	
	private Service service;
	
	public MapObjectivesTableModel() {
		this.service = new AbstractScheduledService() {
			@Override
			protected void runOneIteration() throws Exception {
				int row = 0;
				for (IWVWObjective content : MapObjectivesTableModel.this.content) {
					if(content.getRemainingBuffDuration(TimeUnit.SECONDS) > 0) {
						MapObjectivesTableModel.this.fireTableRowsUpdated(row, row);
					}
					row++;
				}
			}

			@Override
			protected Scheduler scheduler() {
				return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.SECONDS);
			}
			
		};
		this.service.startAndWait();
	}

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
		return this.content.size();
	}

	public Comparator<?> getColumnComparator(int col){
		switch(col) {
			default:
				return new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						return String.valueOf(o1).compareTo(String.valueOf(o2));
					}
				};
		}	
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return this.content.get(rowIndex).getLabel().get();
			case 1:
				return this.content.get(rowIndex).getType().getLabel();
			case 2:
				return this.content.get(rowIndex).getOwner().get().getName().get();
			case 3:
				final Optional<Calendar> calendar = this.content.get(rowIndex).getEndOfBuffTimestamp();
				if (calendar.isPresent()) {
					return DF.format(calendar.get().getTime());
				} else {
					return "";
				}
			case 4:
				if(this.content.get(rowIndex).getEndOfBuffTimestamp().isPresent()) {
					return this.content.get(rowIndex).getRemainingBuffDuration(TimeUnit.SECONDS)+"s";
				}else {
					return "";
				}
			default:
				throw new IllegalArgumentException("Unknown column: " + columnIndex);
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
