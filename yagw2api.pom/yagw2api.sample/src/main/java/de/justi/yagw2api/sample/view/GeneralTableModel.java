package de.justi.yagw2api.sample.view;

import javax.swing.table.AbstractTableModel;

import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;

public class GeneralTableModel extends AbstractTableModel implements IWVWMatchListener {

	static final long	serialVersionUID	= -8573658641105845856L;

	private IWVWMatch	match;

	public void wireUp(IWVWWrapper wrapper, IWVWMatch match) {
		this.match = match;
		wrapper.unregisterWVWMatchListener(this);
		wrapper.registerWVWMatchListener(match, this);
		this.fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		if (match != null) {
			return 10;
		} else {
			return 0;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (this.match != null) {
			switch (rowIndex) {
				case 0:
					switch (columnIndex) {
						case 0:
							return "Ewige Schlachtfelder";
						case 1:
							return this.match.getCenterMap().getScores().getGreenScore();
						case 2:
							return this.match.getCenterMap().getScores().getBlueScore();
						case 3:
							return this.match.getCenterMap().getScores().getRedScore();
						default:
							return "";
					}
				case 1:
					switch (columnIndex) {
						case 0:
							return this.match.getGreenWorld().getName().get()+" Grenzlande";
						case 1:
							return this.match.getGreenMap().getScores().getGreenScore();
						case 2:
							return this.match.getGreenMap().getScores().getBlueScore();
						case 3:
							return this.match.getGreenMap().getScores().getRedScore();
						default:
							return "";
					}
				case 2:
					switch (columnIndex) {
						case 0:
							return this.match.getBlueWorld().getName().get()+" Grenzlande";
						case 1:
							return this.match.getBlueMap().getScores().getGreenScore();
						case 2:
							return this.match.getBlueMap().getScores().getBlueScore();
						case 3:
							return this.match.getBlueMap().getScores().getRedScore();
						default:
							return "";
					}
				case 3:
					switch (columnIndex) {
						case 0:
							return this.match.getRedWorld().getName().get()+" Grenzlande";
						case 1:
							return this.match.getRedMap().getScores().getGreenScore();
						case 2:
							return this.match.getRedMap().getScores().getBlueScore();
						case 3:
							return this.match.getRedMap().getScores().getRedScore();
						default:
							return "";
					}
				case 4:
					switch (columnIndex) {
						case 0:
							return "Gesamt";
						case 1:
							return this.match.getScores().getGreenScore();
						case 2:
							return this.match.getScores().getBlueScore();
						case 3:
							return this.match.getScores().getRedScore();
						default:
							return "";
					}
				case 5:
					switch (columnIndex) {
						case 0:
							return "Ewige Schlachtfelder";
						case 1:
							return this.match.getCenterMap().calculateGreenTick();
						case 2:
							return this.match.getCenterMap().calculateBlueTick();
						case 3:
							return this.match.getCenterMap().calculateRedTick();
						default:
							return "";
					}
				case 6:
					switch (columnIndex) {
						case 0:
							return this.match.getGreenWorld().getName().get()+" Grenzlande";
						case 1:
							return this.match.getGreenMap().calculateGreenTick();
						case 2:
							return this.match.getGreenMap().calculateBlueTick();
						case 3:
							return this.match.getGreenMap().calculateRedTick();
						default:
							return "";
					}
				case 7:
					switch (columnIndex) {
						case 0:
							return this.match.getBlueWorld().getName().get()+" Grenzlande";
						case 1:
							return this.match.getBlueMap().calculateGreenTick();
						case 2:
							return this.match.getBlueMap().calculateBlueTick();
						case 3:
							return this.match.getBlueMap().calculateRedTick();
						default:
							return "";
					}
				case 8:
					switch (columnIndex) {
						case 0:
							return this.match.getRedWorld().getName().get()+" Grenzlande";
						case 1:
							return this.match.getRedMap().calculateGreenTick();
						case 2:
							return this.match.getRedMap().calculateBlueTick();
						case 3:
							return this.match.getRedMap().calculateRedTick();
						default:
							return "";
					}
				case 9:
					switch (columnIndex) {
						case 0:
							return "Gesamt";
						case 1:
							return this.match.calculateGreenTick();
						case 2:
							return this.match.calculateBlueTick();
						case 3:
							return this.match.calculateRedTick();
						default:
							return "";
					}
				default:
					return "";
			}
		} else {
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
}
