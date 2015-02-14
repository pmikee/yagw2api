package de.justi.yagw2api.explorer.model;

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

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.Service;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjective;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.IWorld;

public final class MapObjectivesTableModel extends AbstractTableModel implements IWVWMapListener {
	private static final long serialVersionUID = -4657108157862724940L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapObjectivesTableModel.class);

	final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private final List<IWVWObjective> content = new CopyOnWriteArrayList<IWVWObjective>();

	private Service service;

	public MapObjectivesTableModel() {
		this.service = new AbstractScheduledService() {
			@Override
			protected void runOneIteration() throws Exception {
				int row = 0;
				for (IWVWObjective content : MapObjectivesTableModel.this.content) {
					if (content.getRemainingBuffDuration(TimeUnit.SECONDS) > 0) {
						MapObjectivesTableModel.this.fireTableCellUpdated(row, 6);
					}
					row++;
				}
			}

			@Override
			protected Scheduler scheduler() {
				return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.SECONDS);
			}

		};
		this.service.startAsync();
	}

	public void wireUp(IWVWWrapper wrapper, IWVWMap map, IWVWMap... maps) {
		this.content.clear();
		this.content.addAll(map.getObjectives());
		wrapper.unregisterWVWMapListener(this);
		wrapper.registerWVWMapListener(map, this);
		for (IWVWMap additionalMap : maps) {
			this.content.addAll(additionalMap.getObjectives());
			wrapper.registerWVWMapListener(additionalMap, this);
		}
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 9;
	}

	@Override
	public int getRowCount() {
		return this.content.size();
	}

	public Comparator<?> getColumnComparator(int col) {
		switch (col) {
			case 3: // objective points
				return new Comparator<Integer>() {
					@Override
					public int compare(Integer o1, Integer o2) {
						return o1.compareTo(o2);
					}
				};
			case 6: // remaining buff duration
				return new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						final String o1b = "".equals(o1.toString()) ? "-1" : o1.toString();
						final String o2b = "".equals(o2.toString()) ? "-1" : o2.toString();
						return Integer.valueOf(o1b).compareTo(Integer.valueOf(o2b));
					}
				};
			default:
				return new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						return String.valueOf(o1).compareTo(String.valueOf(o2));
					}
				};
		}
	}

	public Optional<IWVWObjective> getObjectiveForRow(int row) {
		if ((row < 0) || (row >= this.content.size())) {
			return Optional.absent();
		} else {
			return Optional.fromNullable(this.content.get(row));
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final Optional<IWVWObjective> objective = this.getObjectiveForRow(rowIndex);
		checkArgument(objective.isPresent());
		switch (columnIndex) {
			case 0:
				return objective.get().getMap().get().getType().getLabel(YAGW2APIArenanet.INSTANCE.getCurrentLocale()).or("");
			case 1:
				return objective.get().getLabel().get();
			case 2:
				return objective.get().getType().getLabel();
			case 3:
				return objective.get().getType().getPoints();
			case 4:
				final Optional<IWorld> owner = objective.get().getOwner();
				if(owner.isPresent()){
					return objective.get().getOwner().get().getName().get();
				}else{
					return "";
				}
			case 5:
				final Optional<LocalDateTime> endOfBuff = objective.get().getEndOfBuffTimestamp();
				if (endOfBuff.isPresent()) {
					return endOfBuff.get().format(DF);
				} else {
					return "";
				}
			case 6:
				if (objective.get().getEndOfBuffTimestamp().isPresent()) {
					return objective.get().getRemainingBuffDuration(TimeUnit.SECONDS);
				} else {
					return "";
				}
			case 7:
				if (objective.get().getClaimedByGuild().isPresent()) {
					return objective.get().getClaimedByGuild().get().getName();
				} else {
					return "";
				}
			case 8:
				if (objective.get().getClaimedByGuild().isPresent()) {
					return objective.get().getClaimedByGuild().get().getTag();
				} else {
					return "";
				}
			default:
				throw new IllegalArgumentException("Unknown column: " + columnIndex);
		}
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
	}

	private void updateObjective(final IWVWObjective objective) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					checkState(SwingUtilities.isEventDispatchThread());
					checkState(MapObjectivesTableModel.this.content.contains(objective));
					final int row = MapObjectivesTableModel.this.content.indexOf(objective);
					checkState(MapObjectivesTableModel.this.content.contains(objective));
					checkState(row >= 0, row + " should be greater than or equal to 0");
					checkState(row < MapObjectivesTableModel.this.content.size(), row + " should be smaller than " + MapObjectivesTableModel.this.content.size());
					checkState(row < MapObjectivesTableModel.this.getRowCount(), row + " should be smaller than " + MapObjectivesTableModel.this.getRowCount());

					MapObjectivesTableModel.this.fireTableDataChanged();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			LOGGER.error("Failed to update " + IWVWObjective.class.getSimpleName(), e);
		}

	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		this.updateObjective(event.getObjective());
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		this.updateObjective(event.getObjective());
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
		this.updateObjective(event.getObjective());
	}

	@Override
	public void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event) {
		this.updateObjective(event.getObjective());
	}

}
