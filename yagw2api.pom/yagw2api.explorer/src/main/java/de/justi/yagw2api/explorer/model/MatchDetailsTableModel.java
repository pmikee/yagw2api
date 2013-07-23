package de.justi.yagw2api.explorer.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.IWVWInitializedMatchEvent;
import de.justi.yagw2api.wrapper.IWVWMap;
import de.justi.yagw2api.wrapper.IWVWMapListener;
import de.justi.yagw2api.wrapper.IWVWMapScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWMatch;
import de.justi.yagw2api.wrapper.IWVWMatchListener;
import de.justi.yagw2api.wrapper.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveCaptureEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveClaimedEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveEndOfBuffEvent;
import de.justi.yagw2api.wrapper.IWVWObjectiveUnclaimedEvent;
import de.justi.yagw2api.wrapper.IWVWWrapper;

public final class MatchDetailsTableModel extends AbstractTableModel implements IWVWMatchListener, IWVWMapListener {
	private static final Logger LOGGER = Logger.getLogger(MatchDetailsTableModel.class);
	private static NumberFormat NF = new DecimalFormat("###,###,##0", DecimalFormatSymbols.getInstance(YAGW2APIArenanet.INSTANCE.getCurrentLocale()));
	static final long serialVersionUID = -8573658641105845856L;

	private IWVWMatch match;

	public void wireUp(IWVWWrapper wrapper, IWVWMatch match, IWVWMap... maps) {
		checkNotNull(wrapper);
		checkNotNull(match);
		checkNotNull(maps);
		LOGGER.trace("Going to wire " + this + " up using wrapper=" + wrapper + " match=" + match.getId() + " maps=" + maps.length);
		this.match = match;
		wrapper.unregisterWVWMatchListener(this);
		wrapper.unregisterWVWMapListener(this);
		wrapper.registerWVWMatchListener(match, this);
		for (IWVWMap map : maps) {
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
						return this.match != null ? NF.format(this.match.getCenterMap().getScores().getGreenScore()) : "";
					case 2:
						return this.match != null ? this.match.getCenterMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? NF.format(this.match.getCenterMap().getScores().getBlueScore()) : "";
					case 4:
						return this.match != null ? this.match.getCenterMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? NF.format(this.match.getCenterMap().getScores().getRedScore()) : "";
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
						return this.match != null ? NF.format(this.match.getGreenMap().getScores().getGreenScore()) : "";
					case 2:
						return this.match != null ? this.match.getGreenMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? NF.format(this.match.getGreenMap().getScores().getBlueScore()) : "";
					case 4:
						return this.match != null ? this.match.getGreenMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? NF.format(this.match.getGreenMap().getScores().getRedScore()) : "";
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
						return this.match != null ? NF.format(this.match.getBlueMap().getScores().getGreenScore()) : "";
					case 2:
						return this.match != null ? this.match.getBlueMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? NF.format(this.match.getBlueMap().getScores().getBlueScore()) : "";
					case 4:
						return this.match != null ? this.match.getBlueMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? NF.format(this.match.getBlueMap().getScores().getRedScore()) : "";
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
						return this.match != null ? NF.format(this.match.getRedMap().getScores().getGreenScore()) : "";
					case 2:
						return this.match != null ? this.match.getRedMap().calculateGreenTick() : "";
					case 3:
						return this.match != null ? NF.format(this.match.getRedMap().getScores().getBlueScore()) : "";
					case 4:
						return this.match != null ? this.match.getRedMap().calculateBlueTick() : "";
					case 5:
						return this.match != null ? NF.format(this.match.getRedMap().getScores().getRedScore()) : "";
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
						return this.match != null ? NF.format(this.match.getScores().getGreenScore()) : "";
					case 2:
						return this.match != null ? this.match.calculateGreenTick() : "";
					case 3:
						return this.match != null ? NF.format(this.match.getScores().getBlueScore()) : "";
					case 4:
						return this.match != null ? this.match.calculateBlueTick() : "";
					case 5:
						return this.match != null ? NF.format(this.match.getScores().getRedScore()) : "";
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
		// nothing to do
	}

	@Override
	public void onChangedMapScoreEvent(IWVWMapScoresChangedEvent event) {
		if (this.match.getCenterMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(0, 0);
			this.fireTableRowsUpdated(4, 4);
		} else if (this.match.getGreenMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(1, 1);
			this.fireTableRowsUpdated(4, 4);
		} else if (this.match.getBlueMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(2, 2);
			this.fireTableRowsUpdated(4, 4);
		} else if (this.match.getRedMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(3, 3);
			this.fireTableRowsUpdated(4, 4);
		}
	}

	@Override
	public void onObjectiveCapturedEvent(IWVWObjectiveCaptureEvent event) {
		if (this.match.getCenterMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(0, 0);
			this.fireTableRowsUpdated(4, 4);
		} else if (this.match.getGreenMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(1, 1);
			this.fireTableRowsUpdated(4, 4);
		} else if (this.match.getBlueMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(2, 2);
			this.fireTableRowsUpdated(4, 4);
		} else if (this.match.getRedMap().equals(event.getMap())) {
			this.fireTableRowsUpdated(3, 3);
			this.fireTableRowsUpdated(4, 4);
		}
	}

	@Override
	public void onObjectiveEndOfBuffEvent(IWVWObjectiveEndOfBuffEvent event) {
		// nothing to do
	}

	@Override
	public void onObjectiveClaimedEvent(IWVWObjectiveClaimedEvent event) {
		// nothing to do
	}

	@Override
	public void onObjectiveUnclaimedEvent(IWVWObjectiveUnclaimedEvent event) {
		// nothing to do
	}
}
