package de.justi.yagw2api.sample.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWInitializedMatchEvent;
import de.justi.yagw2api.core.wrapper.model.wvw.events.IWVWMatchScoresChangedEvent;

public final class MatchesTableModel extends AbstractTableModel implements IWVWMatchListener {
	private static final Logger LOGGER = Logger.getLogger(MatchesTableModel.class);
	private static final long serialVersionUID = 267092039654136315L;
	private static final NumberFormat NF = new DecimalFormat("#,###,##0");
	private static final DateFormat DF = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

	private final List<IWVWMatch> matches = Collections.synchronizedList(new CopyOnWriteArrayList<IWVWMatch>());

	public void wireUp(IWVWWrapper wrapper) {
		this.matches.clear();
		this.fireTableDataChanged();
		wrapper.unregisterWVWMatchListener(this);
		wrapper.registerWVWMatchListener(this);
	}

	public Comparator<?> getColumnComparator(int col) {
		switch (col) {
			case 3:
			case 5:
			case 7:
				return new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						try {
							return new Integer(NF.parse(o1).intValue()).compareTo(NF.parse(o2).intValue());
						} catch (ParseException e) {
							return -1;
						}
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
	public int getColumnCount() {
		return 9;
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
		checkState(this.matches.get(matchIndex) != null);
		switch (columnIndex) {
			case 0:
				return this.matches.get(matchIndex).getId();
			case 1:
				return (this.matches.get(matchIndex).getGreenWorld().getWorldLocation().toString().equals("EUROPE") ? "EU" : "NA");
			case 2:
				return (this.matches.get(matchIndex).getGreenWorld().getName().get());
			case 3:
				return NF.format(this.matches.get(matchIndex).getScores().getGreenScore());
			case 4:
				return (this.matches.get(matchIndex).getBlueWorld().getName().get());
			case 5:
				return NF.format(this.matches.get(matchIndex).getScores().getBlueScore());
			case 6:
				return (this.matches.get(matchIndex).getRedWorld().getName().get());
			case 7:
				return NF.format(this.matches.get(matchIndex).getScores().getRedScore());
			case 8:
				checkState(this.matches.get(matchIndex).getStartTimestamp() != null);
				return DF.format(this.matches.get(matchIndex).getStartTimestamp().getTime());
			case 9:
				checkState(this.matches.get(matchIndex).getEndTimestamp() != null);
				return DF.format(this.matches.get(matchIndex).getEndTimestamp().getTime());
			default:
				throw new IllegalArgumentException("Unknown column: " + columnIndex);
		}
	}

	@Override
	public void onMatchScoreChangedEvent(final IWVWMatchScoresChangedEvent event) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					final int rowIndex = MatchesTableModel.this.matches.indexOf(event.getMatch());
					MatchesTableModel.this.fireTableRowsUpdated(rowIndex, rowIndex);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			LOGGER.error("Failed to react to " + event, e);
		}
	}

	@Override
	public void onInitializedMatchForWrapper(final IWVWInitializedMatchEvent event) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					checkState(SwingUtilities.isEventDispatchThread());
					if (!MatchesTableModel.this.matches.contains(event.getMatch())) {
						MatchesTableModel.this.matches.add(event.getMatch());
					}
					MatchesTableModel.this.fireTableRowsInserted(MatchesTableModel.this.matches.size() - 1, MatchesTableModel.this.matches.size() - 1);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			LOGGER.error("Failed to react to " + event, e);
		}
	}

}
