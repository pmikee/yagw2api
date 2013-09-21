package de.justi.yagw2api.explorer.model;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-Application
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import javax.swing.table.AbstractTableModel;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;

import de.justi.yagw2api.gw2stats.IAPIStateDTO;
import de.justi.yagw2api.gw2stats.IGW2StatsService;

public class APIStatusTableModel extends AbstractTableModel {
	private static final long serialVersionUID = -5095699668587612370L;
	private Map<String, IAPIStateDTO> states = null;
	private final IGW2StatsService gw2statsService;

	@Inject
	public APIStatusTableModel(IGW2StatsService gw2statsService) {
		this.gw2statsService = checkNotNull(gw2statsService);
		// TODO refactor this to an event based system -> wrap it
		final AbstractScheduledService service = new AbstractScheduledService() {
			@Override
			protected Scheduler scheduler() {
				return AbstractScheduledService.Scheduler.newFixedDelaySchedule(0, 5000, TimeUnit.MILLISECONDS);
			}

			@Override
			protected void runOneIteration() throws Exception {
				APIStatusTableModel.this.states = APIStatusTableModel.this.gw2statsService.retrieveAPIStates();
				APIStatusTableModel.this.fireTableDataChanged();
			}
		};
		service.start();
	}

	public Comparator<?> getColumnComparator(int col) {
		switch (col) {
			case 3:
			case 4:
			case 5:
				return new Comparator<Object>() {
					@Override
					public int compare(Object o1, Object o2) {
						return Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString()));
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

	@Override
	public int getRowCount() {
		return this.states != null ? this.states.size() : 0;
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		final IAPIStateDTO state;
		if (this.states != null) {
			final SortedMap<String, IAPIStateDTO> statesMap = Maps.newTreeMap();
			statesMap.putAll(this.states);
			final List<String> keys = new ArrayList<String>(statesMap.keySet());
			checkState(rowIndex >= 0);
			checkState(rowIndex < keys.size());
			state = statesMap.get(keys.get(rowIndex));
			if (state != null) {
				switch (columnIndex) {
					case 0:
						return keys.get(rowIndex);
					case 1:
						return state.getStatus();
					case 2:
						if (state.getDescription().isPresent()) {
							return state.getDescription().get().getDescription();
						} else {
							return "";
						}
					case 3:
						return state.getPing();
					case 4:
						return state.getRetrieve();
					case 5:
						return state.getRecords();
					case 6:
						return state.getTime();
					default:
						return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
