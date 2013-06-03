package de.justi.yagw2api.sample.view;

import java.util.Set;

import javax.swing.table.AbstractTableModel;

import de.justi.yagw2api.core.wrapper.IWVWMatchListener;
import de.justi.yagw2api.core.wrapper.IWVWWrapper;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWObjective;
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
		return 11;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (match != null) {
			Set<IWVWObjective> centerMapObjectives = match.getCenterMap().getObjectives();
			int greenTickOnCenterMap = 0;
			int blueTickOnCenterMap = 0;
			int redTickOnCenterMap = 0;
			for (IWVWObjective objective : centerMapObjectives) {
				if (objective.getOwner().get().equals(match.getGreenWorld())) {
					greenTickOnCenterMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getBlueWorld())) {
					blueTickOnCenterMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getRedWorld())) {
					redTickOnCenterMap += objective.getType().getPoints();
				}
			}
			Set<IWVWObjective> greenMapObjectives = match.getGreenMap().getObjectives();
			int greenTickOnGreenMap = 0;
			int blueTickOnGreenMap = 0;
			int redTickOnGreenMap = 0;
			for (IWVWObjective objective : greenMapObjectives) {
				if (objective.getOwner().get().equals(match.getGreenWorld())) {
					greenTickOnGreenMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getBlueWorld())) {
					blueTickOnGreenMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getRedWorld())) {
					redTickOnGreenMap += objective.getType().getPoints();
				}
			}
			Set<IWVWObjective> blueMapObjectives = match.getBlueMap().getObjectives();
			int greenTickOnBlueMap = 0;
			int blueTickOnBlueMap = 0;
			int redTickOnBlueMap = 0;
			for (IWVWObjective objective : blueMapObjectives) {
				if (objective.getOwner().get().equals(match.getGreenWorld())) {
					greenTickOnBlueMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getBlueWorld())) {
					blueTickOnBlueMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getRedWorld())) {
					redTickOnBlueMap += objective.getType().getPoints();
				}
			}
			Set<IWVWObjective> redMapObjectives = match.getRedMap().getObjectives();
			int greenTickOnRedMap = 0;
			int blueTickOnRedMap = 0;
			int redTickOnRedMap = 0;
			for (IWVWObjective objective : redMapObjectives) {
				if (objective.getOwner().get().equals(match.getGreenWorld())) {
					greenTickOnRedMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getBlueWorld())) {
					blueTickOnRedMap += objective.getType().getPoints();
				} else if (objective.getOwner().get().equals(match.getRedWorld())) {
					redTickOnRedMap += objective.getType().getPoints();
				}
			}
			switch (rowIndex) {
				case 0:
					switch (columnIndex) {
						case 0:
							return "Servername";
						case 1:
							return match.getGreenWorld().getName().get();
						case 2:
							return match.getBlueWorld().getName().get();
						case 3:
							return match.getRedWorld().getName().get();
						default:
							return "";
					}
				case 1:
					switch (columnIndex) {
						case 0:
							return "Punkte Ewige";
						case 1:
							return match.getCenterMap().getScores().getGreenScore();
						case 2:
							return match.getCenterMap().getScores().getBlueScore();
						case 3:
							return match.getCenterMap().getScores().getRedScore();
						default:
							return "";
					}
				case 2:
					switch (columnIndex) {
						case 0:
							return "Punkte Grünlande";
						case 1:
							return match.getGreenMap().getScores().getGreenScore();
						case 2:
							return match.getGreenMap().getScores().getBlueScore();
						case 3:
							return match.getGreenMap().getScores().getRedScore();
						default:
							return "";
					}
				case 3:
					switch (columnIndex) {
						case 0:
							return "Punkte Blaulande";
						case 1:
							return match.getBlueMap().getScores().getGreenScore();
						case 2:
							return match.getBlueMap().getScores().getBlueScore();
						case 3:
							return match.getBlueMap().getScores().getRedScore();
						default:
							return "";
					}
				case 4:
					switch (columnIndex) {
						case 0:
							return "Punkte Rotlande";
						case 1:
							return match.getRedMap().getScores().getGreenScore();
						case 2:
							return match.getRedMap().getScores().getBlueScore();
						case 3:
							return match.getRedMap().getScores().getRedScore();
						default:
							return "";
					}
				case 5:
					switch (columnIndex) {
						case 0:
							return "Punkte Gesamt";
						case 1:
							return match.getScores().getGreenScore();
						case 2:
							return match.getScores().getBlueScore();
						case 3:
							return match.getScores().getRedScore();
						default:
							return "";
					}
				case 6:
					switch (columnIndex) {
						case 0:
							return "Tick Ewige";
						case 1:
							return greenTickOnCenterMap;
						case 2:
							return blueTickOnCenterMap;
						case 3:
							return redTickOnCenterMap;
						default:
							return "";
					}
				case 7:
					switch (columnIndex) {
						case 0:
							return "Tick Grünlande";
						case 1:
							return greenTickOnGreenMap;
						case 2:
							return blueTickOnGreenMap;
						case 3:
							return redTickOnGreenMap;
						default:
							return "";
					}
				case 8:
					switch (columnIndex) {
						case 0:
							return "Tick Blaulande";
						case 1:
							return greenTickOnBlueMap;
						case 2:
							return blueTickOnBlueMap;
						case 3:
							return redTickOnBlueMap;
						default:
							return "";
					}
				case 9:
					switch (columnIndex) {
						case 0:
							return "Tick Rotlande";
						case 1:
							return greenTickOnRedMap;
						case 2:
							return blueTickOnRedMap;
						case 3:
							return redTickOnRedMap;
						default:
							return "";
					}
				case 10:
					switch (columnIndex) {
						case 0:
							return "Tick Gesamt";
						case 1:
							return greenTickOnCenterMap+greenTickOnGreenMap+greenTickOnBlueMap+greenTickOnRedMap;
						case 2:
							return blueTickOnCenterMap+blueTickOnGreenMap+blueTickOnBlueMap+blueTickOnRedMap;
						case 3:
							return redTickOnCenterMap+redTickOnGreenMap+redTickOnBlueMap+redTickOnRedMap;
						default:
							return "";
					}
				default:
					return "";
			}
		} else {
			return "select match";
		}

	}

	@Override
	public void notifyAboutMatchScoreChangedEvent(IWVWMatchScoresChangedEvent event) {
		this.fireTableDataChanged();
	}
}
