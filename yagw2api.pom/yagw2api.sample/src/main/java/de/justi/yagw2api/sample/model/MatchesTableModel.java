package de.justi.yagw2api.sample.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;
import de.justi.yagw2api.sample.Main;

public class MatchesTableModel extends AbstractTableModel implements IWVWMatchListener {
	private static final Logger	LOGGER				= Logger.getLogger(MatchesTableModel.class);
	private static final long	serialVersionUID	= 267092039654136315L;

	private List<IWVWMatch>		matches				= Collections.synchronizedList(new CopyOnWriteArrayList<IWVWMatch>());

	public void wireUp(IWVWWrapper wrapper) {
		this.matches.clear();
		this.matches.addAll(wrapper.getAllMatches());
		this.fireTableDataChanged();
		wrapper.unregisterWVWMatchListener(this);
		wrapper.registerWVWMatchListener(this);
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public int getRowCount() {
		return this.matches.size();
	}

	private int getMatchIndexForRow(int row) {
		checkArgument(row >= 0);
		checkArgument(row < this.getRowCount(), row + " has to be smaller than " + this.getRowCount());
		return row;
	}

	public Optional<IWVWMatch> getMatch(int row) {
		checkArgument(row >= -1);
		if (row < 0) {
			return Optional.absent();
		} else {
			checkState(row >= 0, "row=" + row + " has to be greater or equal to 0");
			final int matchIndex = this.getMatchIndexForRow(row);
			checkState(matchIndex >= 0, "matchIndex=" + matchIndex + " has to be greater or equal to 0");
			final IWVWMatch match = this.matches.get(matchIndex);
			return Optional.fromNullable(match);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		final int matchIndex = this.getMatchIndexForRow(rowIndex);
		switch (columnIndex) {
			case 0:
				return (this.matches.get(matchIndex).getGreenWorld().getName().get());
			case 2:
				return (this.matches.get(matchIndex).getBlueWorld().getName().get());
			case 4:
				return (this.matches.get(matchIndex).getRedWorld().getName().get());
			case 1:
				return this.matches.get(matchIndex).getScores().getGreenScore();
			case 3:
				return this.matches.get(matchIndex).getScores().getBlueScore();
			case 5:
				return this.matches.get(matchIndex).getScores().getRedScore();
			default:
				throw new IllegalArgumentException("Unknown column: " + columnIndex);
		}
	}

	@Override
	public void notifyAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		final int rowIndex = this.matches.indexOf(event.getMatch());
		this.fireTableRowsUpdated(rowIndex, rowIndex);
	}

}
