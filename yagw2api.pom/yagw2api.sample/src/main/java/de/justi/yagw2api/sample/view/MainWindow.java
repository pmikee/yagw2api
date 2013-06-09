package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWMapType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWMapType;
import de.justi.yagw2api.sample.Main;
import de.justi.yagw2api.sample.model.GeneralTableModel;
import de.justi.yagw2api.sample.model.MapObjectivesTableModel;
import de.justi.yagw2api.sample.model.MatchesTableModel;
import de.justi.yagw2api.sample.renderer.GeneralTableCellRenderer;
import de.justi.yagw2api.sample.renderer.ObjectiveTableCellRenderer;

public class MainWindow extends AbstractWindow {
	private static final long serialVersionUID = -6500541020042114865L;
	private static final Logger LOGGER = Logger.getLogger(MainWindow.class);
	public static final Color ETERNAL_BATTLEGROUNDS_FG = new Color(200, 130, 0);
	public static final Color ETERNAL_BATTLEGROUNDS_BG = new Color(200, 130, 0, 100);
	public static final Color GREEN_WORLD_FG = new Color(70, 152, 42);
	public static final Color GREEN_WORLD_BG = new Color(70, 152, 42, 100);
	public static final Color BLUE_WORLD_FG = new Color(35, 129, 199);
	public static final Color BLUE_WORLD_BG = new Color(35, 129, 199, 100);
	public static final Color RED_WORLD_FG = new Color(175, 25, 10);
	public static final Color RED_WORLD_BG = new Color(175, 25, 10, 100);

	private final JTabbedPane tabPane;

	private final MatchesTableModel matchModel;
	private final GeneralTableModel generalModel;
	private final MapObjectivesTableModel allMapsModel;
	private final MapObjectivesTableModel eternalMapModel;
	private final MapObjectivesTableModel greenMapModel;
	private final MapObjectivesTableModel blueMapModel;
	private final MapObjectivesTableModel redMapModel;

	private Optional<IWVWMatch> selectedMatch = Optional.absent();

	private JTable selectionTable;
	private JTable generalTable;
	private JTable allMapsTable;
	private JTable eternalTable;
	private JTable greenTable;
	private JTable blueTable;
	private JTable redTable;

	public MainWindow() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(1024, 768));

		this.matchModel = new MatchesTableModel();
		this.generalModel = new GeneralTableModel();
		this.allMapsModel = new MapObjectivesTableModel();
		this.eternalMapModel = new MapObjectivesTableModel();
		this.greenMapModel = new MapObjectivesTableModel();
		this.blueMapModel = new MapObjectivesTableModel();
		this.redMapModel = new MapObjectivesTableModel();

		final JPanel selectionPanel = this.initSelectionPanel();
		final JPanel generalPanel = this.initGeneralPanel();

		final JPanel eternalPanel = new JPanel();
		this.initMapPanel(eternalPanel, eternalMapModel, eternalTable, WVWMapType.CENTER);
		final JPanel greenPanel = new JPanel();
		this.initMapPanel(greenPanel, greenMapModel, greenTable, WVWMapType.GREEN);
		final JPanel bluePanel = new JPanel();
		this.initMapPanel(bluePanel, blueMapModel, blueTable, WVWMapType.BLUE);
		final JPanel redPanel = new JPanel();
		this.initMapPanel(redPanel, redMapModel, redTable, WVWMapType.RED);

		this.tabPane = new JTabbedPane();
		this.getTabPane().addTab("Spielpartien", selectionPanel);
		getTabPane().addTab("Alle Karten", generalPanel);
		this.getTabPane().addTab("Ewige Schlachtfelder", eternalPanel);
		this.getTabPane().setForegroundAt(2, ETERNAL_BATTLEGROUNDS_FG);
		this.getTabPane().addTab("Grüne Grenzlande", greenPanel);
		this.getTabPane().setForegroundAt(3, GREEN_WORLD_FG);
		this.getTabPane().addTab("Blaue Grenzlande", bluePanel);
		this.getTabPane().setForegroundAt(4, BLUE_WORLD_FG);
		this.getTabPane().addTab("Rote Grenzlande", redPanel);
		this.getTabPane().setForegroundAt(5, RED_WORLD_FG);

		this.getContentPanel().add(getTabPane(), BorderLayout.CENTER);

		final JPanel bottomPanel = new JPanel(new BorderLayout());
		String[] generalHeader = { "", "Punkte (Grün)", "Punktezuwachs (Grün)", "Punkte (Blau)", "Punktezuwachs (Blau)", "Punkte (Rot)", "Punktezuwachs (Rot)" };
		final TableColumnModel tcm = this.newTCM(generalHeader);
		this.generalTable = new JTable(this.getGeneralModel(), tcm);
		this.generalTable.setDefaultRenderer(Object.class, new GeneralTableCellRenderer());
		this.generalTable.getTableHeader().setReorderingAllowed(false);
		this.generalTable.setEnabled(false);
		bottomPanel.add(this.generalTable.getTableHeader(), BorderLayout.NORTH);
		bottomPanel.add(this.generalTable, BorderLayout.CENTER);
		this.getContentPanel().add(bottomPanel, BorderLayout.SOUTH);

		this.pack();
	}

	private TableColumnModel newTCM(String[] header) {

		final TableColumnModel tcm = new DefaultTableColumnModel();
		List<TableColumn> tcl = new ArrayList<>();
		for (int i = 0; i < header.length; i++) {
			tcl.add(new TableColumn(i));
			tcl.get(i).setHeaderValue(header[i]);
			tcm.addColumn(tcl.get(i));
		}

		return tcm;
	}

	private TableColumnModel newMapTCM() {
		final String[] header = { "Karte", "Objekt", "Objekttyp", "Wert", "Besitzer", "Buffende", "Verbleibender Buff", "Gilde", "Gildentag" };
		return newTCM(header);
	}

	private JPanel initSelectionPanel() {
		final JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());

		final String[] header = { "Region", "Grün", "Punkte (Grün)", "Blau", "Punkte (Blau)", "Rot", "Punkte (Rot)", "Start", "Ende" };
		this.selectionTable = new JTable(this.matchModel, newTCM(header));
		this.selectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionPanel.add(new JScrollPane(this.selectionTable), BorderLayout.CENTER);

		final TableRowSorter<MatchesTableModel> sorter = new TableRowSorter<MatchesTableModel>(this.matchModel);
		this.selectionTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for (int col = 0; col < this.matchModel.getColumnCount(); col++) {
			sorter.setComparator(col, this.matchModel.getColumnComparator(col));
		}

		this.selectionTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(final ListSelectionEvent e) {
				SwingUtilities.invokeLater(new Runnable() { 
					@Override
					public void run() {
						final int index = selectionTable.getSelectedRow();
						final Optional<IWVWMatch> matchOptional = MainWindow.this.matchModel.getMatch(index);
						if (matchOptional.isPresent()) {
							LOGGER.debug("Incoming selection event [" + index + "] -> match=" + matchOptional.get().getId());
							if (!selectedMatch.equals(matchOptional)) {
								selectedMatch = matchOptional;
								final IWVWMatch match = matchOptional.get();
								LOGGER.info("NEW selected match=" + match.getId());
								MainWindow.this.getAllMapsModel().wireUp(Main.getWrapper(), match.getCenterMap(), match.getGreenMap(), match.getBlueMap(), match.getRedMap());
								MainWindow.this.getGeneralModel().wireUp(Main.getWrapper(), match, match.getCenterMap(), match.getGreenMap(), match.getBlueMap(), match.getRedMap());
								MainWindow.this.getEternalMapModel().wireUp(Main.getWrapper(), match.getCenterMap());
								MainWindow.this.getGreenMapModel().wireUp(Main.getWrapper(), match.getGreenMap());
								MainWindow.this.getBlueMapModel().wireUp(Main.getWrapper(), match.getBlueMap());
								MainWindow.this.getRedMapModel().wireUp(Main.getWrapper(), match.getRedMap());

								if (match.getGreenWorld().getName().isPresent()) {
									MainWindow.this.getTabPane().setTitleAt(3, match.getGreenWorld().getName().get() + " Grenzlande");
								} else {
									MainWindow.this.getTabPane().setTitleAt(3, "Grüne Grenzlande");
								}
								if (match.getBlueWorld().getName().isPresent()) {
									MainWindow.this.getTabPane().setTitleAt(4, match.getBlueWorld().getName().get() + " Grenzlande");
								} else {
									MainWindow.this.getTabPane().setTitleAt(4, "Blaue Grenzlande");
								}
								if (match.getRedWorld().getName().isPresent()) {
									MainWindow.this.getTabPane().setTitleAt(5, match.getRedWorld().getName().get() + " Grenzlande");
								} else {
									MainWindow.this.getTabPane().setTitleAt(5, "Rote Grenzlande");
								}
								LOGGER.debug("Wired everything up for new selected match=" + match.getId());
							} else {
								LOGGER.debug("Already selected match=" + matchOptional.get().getId());
							}
						}
					}
				});

			}
		});
		return selectionPanel;
	}

	private JPanel initGeneralPanel() {
		final JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BorderLayout());

		this.allMapsTable = new JTable(this.getAllMapsModel(), this.newMapTCM());
		this.allMapsTable.setDefaultRenderer(Object.class, new ObjectiveTableCellRenderer());

		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(this.getAllMapsModel());
		this.allMapsTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for (int col = 0; col < this.getAllMapsModel().getColumnCount(); col++) {
			sorter.setComparator(col, this.getAllMapsModel().getColumnComparator(col));
		}
		generalPanel.add(new JScrollPane(this.allMapsTable), BorderLayout.CENTER);
		return generalPanel;
	}

	private JPanel initMapPanel(JPanel mapPanel, MapObjectivesTableModel mapTableModel, JTable mapTable, IWVWMapType mapType) {
		mapPanel.setLayout(new BorderLayout());

		mapTable = new JTable(mapTableModel, this.newMapTCM());
		mapTable.setDefaultRenderer(Object.class, new ObjectiveTableCellRenderer());

		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(mapTableModel);
		mapTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for (int col = 0; col < mapTableModel.getColumnCount(); col++) {
			sorter.setComparator(col, mapTableModel.getColumnComparator(col));
		}

		mapPanel.add(new JScrollPane(mapTable), BorderLayout.CENTER);
		return mapPanel;
	}

	public MatchesTableModel getMatchModel() {
		return this.matchModel;
	}

	public MapObjectivesTableModel getEternalMapModel() {
		return this.eternalMapModel;
	}

	public MapObjectivesTableModel getGreenMapModel() {
		return this.greenMapModel;
	}

	public MapObjectivesTableModel getBlueMapModel() {
		return this.blueMapModel;
	}

	public MapObjectivesTableModel getRedMapModel() {
		return this.redMapModel;
	}

	public JTabbedPane getTabPane() {
		return tabPane;
	}

	public MapObjectivesTableModel getAllMapsModel() {
		return allMapsModel;
	}

	public GeneralTableModel getGeneralModel() {
		return generalModel;
	}
}
