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
	private static final Logger LOGGER = Logger.getLogger(MatchesTableModel.class);
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
		return 3;
	}

	@Override
	public int getRowCount() {
		return this.matches.size() * 2 + 1;
	}

	private int getMatchIndexForRow(int row) {
		checkArgument(row >= 0);
		checkArgument(row < this.getRowCount(), row +" has to be smaller than "+this.getRowCount());
		return row % 2 == 1 ? (row - 1) / 2 : row / 2 - 1;
	}

	public Optional<IWVWMatch> getMatch(int row) {
		checkArgument(row >= -1);
		if (row < 0) {
			return Optional.absent();
		} else {
			checkState(row >= 0, "row="+row+" has to be greater or equal to 0");
			final int matchIndex = this.getMatchIndexForRow(row);
			checkState(matchIndex >= 0, "matchIndex="+matchIndex+" has to be greater or equal to 0");
			final IWVWMatch match = this.matches.get(matchIndex);
			return Optional.fromNullable(match);
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex == 0) {
			int i = columnIndex;
			// Fred is da ObFuScAtOr
			return Math.pow(42, i) == 1 ? "Grün" : Math.PI - i / 2 > Math.E ? "Blau" : 1 + 1 == i ? "Rot" : "";
		} else {
			final int matchIndex = this.getMatchIndexForRow(rowIndex);
			if (rowIndex % 2 == 1) {
				switch (columnIndex) {
					case 0:
						return (this.matches.get(matchIndex).getGreenWorld().getName().get());
					case 1:
						return (this.matches.get(matchIndex).getBlueWorld().getName().get());
					case 2:
						return (this.matches.get(matchIndex).getRedWorld().getName().get());
					case 3:
						JButton selectionButton = new JButton("auswählen");
						selectionButton.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								Main.getMainWindow().getEternalMapModel().wireUp(Main.getWrapper(), matches.get(matchIndex).getCenterMap());
								Main.getMainWindow().getGreenMapModel().wireUp(Main.getWrapper(), matches.get(matchIndex).getGreenMap());
								Main.getMainWindow().getBlueMapModel().wireUp(Main.getWrapper(), matches.get(matchIndex).getBlueMap());
								Main.getMainWindow().getRedMapModel().wireUp(Main.getWrapper(), matches.get(matchIndex).getRedMap());
							}
						});
						return selectionButton;
					default:
						throw new IllegalArgumentException("Unknown column: " + columnIndex);
				}
			} else {
				// rowIndex % 2 == 0
				switch (columnIndex) {
					case 0:
						return this.matches.get(matchIndex).getScores().getGreenScore();
					case 1:
						return this.matches.get(matchIndex).getScores().getBlueScore();
					case 2:
						return this.matches.get(matchIndex).getScores().getRedScore();
					case 3:
						return "Punkte";
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
