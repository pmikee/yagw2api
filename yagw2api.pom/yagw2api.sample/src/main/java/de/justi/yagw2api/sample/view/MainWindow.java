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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.google.common.base.Optional;
import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.JGraphLayout;
import com.jgraph.layout.tree.JGraphRadialTreeLayout;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.core.wrapper.model.wvw.types.IWVWMapType;
import de.justi.yagw2api.core.wrapper.model.wvw.types.WVWMapType;
import de.justi.yagw2api.sample.Main;
import de.justi.yagw2api.sample.model.GeneralTableModel;
import de.justi.yagw2api.sample.model.MapObjectivesTableModel;
import de.justi.yagw2api.sample.model.MatchesTableModel;

public class MainWindow extends AbstractWindow {
	private static final long serialVersionUID = -6500541020042114865L;
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
		final JPanel graphicMapTestPanel = this.initGraphicMapTestPanel();

		this.tabPane = new JTabbedPane();
		this.getTabPane().addTab("Spielpartien", selectionPanel);
		getTabPane().addTab("Alle Karten", generalPanel);
		this.getTabPane().addTab("Ewige Schlachtfelder", eternalPanel);
		this.getTabPane().addTab("Grüne Grenzlande", greenPanel);
		this.getTabPane().setForegroundAt(3, GREEN_WORLD_FG);
		this.getTabPane().addTab("Blaue Grenzlande", bluePanel);
		this.getTabPane().setForegroundAt(4, BLUE_WORLD_FG);
		this.getTabPane().addTab("Rote Grenzlande", redPanel);
		this.getTabPane().setForegroundAt(5, RED_WORLD_FG);
		this.getTabPane().addTab("MapTest", graphicMapTestPanel);

		this.getContentPanel().add(getTabPane(), BorderLayout.CENTER);

		final JPanel bottomPanel = new JPanel(new BorderLayout());
		String[] generalHeader = { "", "Punkte (Grün)", "Punktezuwachs (Grün)", "Punkte (Blau)", "Punktezuwachs (Blau)", "Punkte (Rot)", "Punktezuwachs (Rot)" };
		final TableColumnModel tcm = this.newTCM(generalHeader);
		this.generalTable = new JTable(this.getGeneralModel(), tcm);
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
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					final Optional<IWVWMatch> match = MainWindow.this.matchModel.getMatch(selectionTable.convertRowIndexToModel(e.getFirstIndex()));
					if (match.isPresent()) {
						MainWindow.this.getAllMapsModel().wireUp(Main.getWrapper(), match.get().getCenterMap(), match.get().getGreenMap(), match.get().getBlueMap(), match.get().getRedMap());
						MainWindow.this.getGeneralModel().wireUp(Main.getWrapper(), match.get(), match.get().getCenterMap(), match.get().getGreenMap(), match.get().getBlueMap(),
								match.get().getRedMap());
						MainWindow.this.getEternalMapModel().wireUp(Main.getWrapper(), match.get().getCenterMap());
						MainWindow.this.getGreenMapModel().wireUp(Main.getWrapper(), match.get().getGreenMap());
						MainWindow.this.getBlueMapModel().wireUp(Main.getWrapper(), match.get().getBlueMap());
						MainWindow.this.getRedMapModel().wireUp(Main.getWrapper(), match.get().getRedMap());
						MainWindow.this.getTabPane().setTitleAt(3, match.get().getGreenWorld().getName().get() + " Grenzlande");
						MainWindow.this.getTabPane().setTitleAt(4, match.get().getBlueWorld().getName().get() + " Grenzlande");
						MainWindow.this.getTabPane().setTitleAt(5, match.get().getRedWorld().getName().get() + " Grenzlande");
						MainWindow.this.repaint();
					}
				}
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

	private JPanel initGraphicMapTestPanel() {
		final JPanel graphicMapTestPanel = new JPanel();
		graphicMapTestPanel.setLayout(new BorderLayout());

		final ListenableUndirectedWeightedGraph<String, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
		graph.addVertex("TEST");
		graph.addVertex("TEST1");
		graph.addVertex("TEST2");
		graph.addVertex("TEST3");
		// graph.addEdge("TEST", "TEST1");
		final JGraphModelAdapter<String, DefaultEdge> graphAdapter = new JGraphModelAdapter<String, DefaultEdge>(graph);
		final JGraph graphComponent = new JGraph(graphAdapter);

		final JScrollPane graphScrollPane = new JScrollPane(graphComponent);

		graphicMapTestPanel.add(graphScrollPane, BorderLayout.CENTER);

		JGraphFacade jgf = new JGraphFacade(graphComponent);
		JGraphLayout layout = new JGraphRadialTreeLayout();
		layout.run(jgf);

		return graphicMapTestPanel;
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
