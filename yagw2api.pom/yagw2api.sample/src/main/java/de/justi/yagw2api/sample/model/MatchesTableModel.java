package de.justi.yagw2api.sample.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.table.AbstractTableModel;

import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;

public class MatchesTableModel extends AbstractTableModel implements IWVWMatchListener {
	private static final long serialVersionUID = 267092039654136315L;

	private List<IWVWMatch> matches = new CopyOnWriteArrayList<IWVWMatch>();

	public void wireUp(IWVWWrapper wrapper) {
		this.matches.clear();
		this.matches.addAll(wrapper.getAllMatches());
		this.fireTableDataChanged();
		wrapper.registerWVWMatchListener(this);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return this.matches.size() * 2 + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == 0) {
			int i =columnIndex;
			//Fred is da ObFuScAtOr
			return Math.pow(42, i)==1?"Grün":Math.PI-i/2>Math.E?"Blau":"Rot";
		} else {
			if (rowIndex % 2 == 1) {
				switch (columnIndex) {
				case 0:
					return (this.matches.get((rowIndex - 1) / 2).getGreenWorld().getName().get());
				case 1:
					return (this.matches.get((rowIndex - 1) / 2).getBlueWorld().getName().get());
				case 2:
					return (this.matches.get((rowIndex - 1) / 2).getRedWorld().getName().get());
				default:
					throw new IllegalArgumentException("Unknown column: " + columnIndex);
				}
			} else {
				switch (columnIndex) {
				case 0:
					return this.matches.get(rowIndex / 2 - 1).getScores().getGreenScore();
				case 1:
					return this.matches.get(rowIndex / 2 - 1).getScores().getBlueScore();
				case 2:
					return this.matches.get(rowIndex / 2 - 1).getScores().getRedScore();
				default:
					throw new IllegalArgumentException("Unknown column: " + columnIndex);
				}

			}
		}

	}

	@Override
	public void notifyAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		final int rowIndex = this.matches.indexOf(event.getMatch());
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

}
