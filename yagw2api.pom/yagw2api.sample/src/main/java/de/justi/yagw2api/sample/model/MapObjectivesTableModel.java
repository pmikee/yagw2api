package de.justi.yagw2api.sample.model;

import static com.google.common.base.Preconditions.checkArgument;

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
	
	public Optional<IWVWObjective> getObjectiveForRow(int row){
		if(row < 0 || row >= this.content.size()) {
			return Optional.absent();
		}else{
			return Optional.fromNullable(this.content.get(row));
		}
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final Optional<IWVWObjective> objective = this.getObjectiveForRow(rowIndex);
		checkArgument(objective.isPresent());
		switch (columnIndex) {
			case 0:
				return objective.get().getLabel().get();
			case 1:
				return objective.get().getType().getLabel();
			case 2:
				return objective.get().getOwner().get().getName().get();
			case 3:
				return objective.get().getType().getPoints();
			case 4:
				final Optional<Calendar> calendar = objective.get().getEndOfBuffTimestamp();
				if (calendar.isPresent()) {
					return DF.format(calendar.get().getTime());
				} else {
					return "";
				}
			case 5:
				if(objective.get().getEndOfBuffTimestamp().isPresent()) {
					return objective.get().getRemainingBuffDuration(TimeUnit.SECONDS)+"s";
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
