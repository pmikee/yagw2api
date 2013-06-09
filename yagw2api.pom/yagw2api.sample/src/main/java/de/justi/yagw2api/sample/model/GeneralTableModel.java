package de.justi.yagw2api.sample.model;

import javax.swing.table.AbstractTableModel;

import de.justi.yagw2api.core.wrapper.IWVWMapListener;
import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWObjectiveEndOfBuffEvent;

public class GeneralTableModel extends AbstractTableModel implements IWVWMatchListener, IWVWMapListener {

	static final long serialVersionUID = -8573658641105845856L;

	private IWVWMatch match;

	public void wireUp(IWVWWrapper wrapper, IWVWMatch match, IWVWMap...wvwMaps) {
		this.match = match;
		wrapper.unregisterWVWMatchListener(this);
		wrapper.registerWVWMatchListener(match, this);
		wrapper.unregisterWVWMapListener(this);
		for (IWVWMap map : wvwMaps) {
			wrapper.registerWVWMapListener(map, this);
		}
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public int getRowCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (rowIndex) {
			case 0:
				switch (columnIndex) {
					case 0:
						return "Ewige Schlachtfelder";
					case 1:
						return this.match != null ? this.match.getCenterMap().getScores().getGreenScore() : "";
					case 2:
						return this.match != null ? this.match.getCenterMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? this.match.getCenterMap().getScores().getBlueScore() : "";
					case 4:
						return this.match != null ? this.match.getCenterMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? this.match.getCenterMap().getScores().getRedScore() : "";
					case 6:
						return this.match != null ? this.match.getCenterMap().calculateRedTick() : "";
					default:
						return "";
				}
			case 1:
				switch (columnIndex) {
					case 0:
						return this.match != null ? this.match.getGreenWorld().getName().get() + " Grenzlande" : "Grüne Grenzlande";
					case 1:
						return this.match != null ? this.match.getGreenMap().getScores().getGreenScore() : "";
					case 2:
						return this.match != null ? this.match.getGreenMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? this.match.getGreenMap().getScores().getBlueScore() : "";
					case 4:
						return this.match != null ? this.match.getGreenMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? this.match.getGreenMap().getScores().getRedScore() : "";
					case 6:
						return this.match != null ? this.match.getGreenMap().calculateRedTick() : "";
					default:
						return "";
				}
			case 2:
				switch (columnIndex) {
					case 0:
						return this.match != null ? this.match.getBlueWorld().getName().get() + " Grenzlande" : "Blaue Grenzlande";
					case 1:
						return this.match != null ? this.match.getBlueMap().getScores().getGreenScore() : "";
					case 2:
						return this.match != null ? this.match.getBlueMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? this.match.getBlueMap().getScores().getBlueScore() : "";
					case 4:
						return this.match != null ? this.match.getBlueMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? this.match.getBlueMap().getScores().getRedScore() : "";
					case 6:
						return this.match != null ? this.match.getBlueMap().calculateRedTick() : "";
					default:
						return "";
				}
			case 3:
				switch (columnIndex) {
					case 0:
						return this.match != null ? this.match.getRedWorld().getName().get() + " Grenzlande" : "Rote Grenzlande";
					case 1:
						return this.match != null ? this.match.getRedMap().getScores().getGreenScore() : "";
					case 2:
						return this.match != null ? this.match.getRedMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? this.match.getRedMap().getScores().getBlueScore() : "";
					case 4:
						return this.match != null ? this.match.getRedMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? this.match.getRedMap().getScores().getRedScore() : "";
					case 6:
						return this.match != null ? this.match.getRedMap().calculateRedTick() : "";
					default:
						return "";
				}
			case 4:
				switch (columnIndex) {
					case 0:
						return "Gesamt";
					case 1:
						return this.match != null ? this.match.getScores().getGreenScore() : "";
					case 2:
						return this.match != null ? this.match.calculateGreenTick() : "";
					case 3:
						return this.match != null ? this.match.getScores().getBlueScore() : "";
					case 4:
						return this.match != null ? this.match.calculateBlueTick() : "";
					case 5:
						return this.match != null ? this.match.getScores().getRedScore() : "";
					case 6:
						return this.match != null ? this.match.calculateRedTick() : "";
					default:
						return "";
				}
			default:
				return "";
		}
	}

	@Override
	public void onMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		this.fireTableDataChanged();
	}

	@Override
	public void onInitializedMatchForWrapper(IWVWInitializedMatchEvent event) {
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		this.fireTableDataChanged();
	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		this.fireTableDataChanged();
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
	}
}
