package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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
import de.justi.yagw2api.sample.Main;
import de.justi.yagw2api.sample.model.GraphicMapModel;
import de.justi.yagw2api.sample.model.MapObjectivesTableModel;
import de.justi.yagw2api.sample.model.MatchesTableModel;

public class MainWindow extends AbstractWindow {
	private static final long serialVersionUID = -6500541020042114865L;

	private final JTabbedPane tabPane;
	
	private final MatchesTableModel matchModel;
	private final MapObjectivesTableModel eternalMapModel;
	private final MapObjectivesTableModel greenMapModel;
	private final MapObjectivesTableModel blueMapModel;
	private final MapObjectivesTableModel redMapModel;

	private JTable selectionTable;
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
		this.eternalMapModel = new MapObjectivesTableModel();
		this.greenMapModel = new MapObjectivesTableModel();
		this.blueMapModel = new MapObjectivesTableModel();
		this.redMapModel = new MapObjectivesTableModel();
		

		final JPanel selectionPanel = this.initSelectionPanel();
		final JPanel generalPanel = this.initGeneralPanel();
		final JPanel eternalPanel = this.initEternalPanel();
		final JPanel greenPanel = this.initGreenPanel();
		final JPanel bluePanel = this.initBluePanel();
		final JPanel redPanel = this.initRedPanel();
		final JPanel graphicMapTestPanel = this.initGraphicMapTestPanel();

		tabPane = new JTabbedPane();
		getTabPane().addTab("Matches", selectionPanel);
		getTabPane().addTab("Übersicht", generalPanel);
		getTabPane().addTab("Ewige Schlachtgründe", eternalPanel);
		getTabPane().addTab("Grüne Grenzlande", greenPanel);
		getTabPane().addTab("Blaue Grenzlande", bluePanel);
		getTabPane().addTab("Rote Grenzlande", redPanel);
		getTabPane().addTab("MapTest", graphicMapTestPanel);

		this.getContentPanel().add(getTabPane(), BorderLayout.CENTER);

		this.pack();
	}



	private TableColumnModel newMapTCM() {

		final TableColumnModel tcm = new DefaultTableColumnModel();
		final TableColumn objectiveColumn = new TableColumn(0);
		objectiveColumn.setHeaderValue("Objekt");
		tcm.addColumn(objectiveColumn);
		final TableColumn objectiveTypeColumn = new TableColumn(1);
		objectiveTypeColumn.setHeaderValue("Objekttyp");
		tcm.addColumn(objectiveTypeColumn);
		final TableColumn ownerColumn = new TableColumn(2);
		ownerColumn.setHeaderValue("Besitzer");
		tcm.addColumn(ownerColumn);
		final TableColumn valueColumn = new TableColumn(3);
		valueColumn.setHeaderValue("Wert");
		tcm.addColumn(valueColumn);
		final TableColumn buffEndColumn = new TableColumn(4);
		buffEndColumn.setHeaderValue("Buffende");
		tcm.addColumn(buffEndColumn);
		final TableColumn remainingBuffColumn = new TableColumn(5);
		remainingBuffColumn.setHeaderValue("Verbleibender Buff");
		tcm.addColumn(remainingBuffColumn);

		return tcm;
	}

	private JPanel initSelectionPanel() {
		final JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());
		selectionPanel.add(new JLabel("Übersicht und Auswahl des Matches"), BorderLayout.NORTH);

		final TableColumnModel tcm = new DefaultTableColumnModel();
		final TableColumn regionColumn = new TableColumn(0);
		regionColumn.setHeaderValue("Region");
		tcm.addColumn(regionColumn);
		final TableColumn greenColumn = new TableColumn(1);
		greenColumn.setHeaderValue("Grün");
		tcm.addColumn(greenColumn);
		final TableColumn greenScoreColumn = new TableColumn(2);
		greenScoreColumn.setHeaderValue("Punkte (Grün)");
		tcm.addColumn(greenScoreColumn);
		final TableColumn blueColumn = new TableColumn(3);
		blueColumn.setHeaderValue("Blau");
		tcm.addColumn(blueColumn);
		final TableColumn blueScoreColumn = new TableColumn(4);
		blueScoreColumn.setHeaderValue("Punkte (Blau)");
		tcm.addColumn(blueScoreColumn);
		final TableColumn redColumn = new TableColumn(5);
		redColumn.setHeaderValue("Rot");
		tcm.addColumn(redColumn);
		final TableColumn redScoreColumn = new TableColumn(6);
		redScoreColumn.setHeaderValue("Punkte (Rot)");
		tcm.addColumn(redScoreColumn);
		final TableColumn startColumn = new TableColumn(7);
		startColumn.setHeaderValue("Start");
		tcm.addColumn(startColumn);
		final TableColumn endColumn = new TableColumn(8);
		endColumn.setHeaderValue("Ende");
		tcm.addColumn(endColumn);
		
		this.selectionTable = new JTable(this.matchModel, tcm);
		this.selectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionPanel.add(new JScrollPane(this.selectionTable), BorderLayout.CENTER);

		final TableRowSorter<MatchesTableModel> sorter = new TableRowSorter<MatchesTableModel>(this.matchModel);
		this.selectionTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for(int col = 0; col < this.matchModel.getColumnCount(); col++) {
			sorter.setComparator(col, this.matchModel.getColumnComparator(col));
		}

		JButton selectionButton = new JButton("auswählen");
		selectionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final Optional<IWVWMatch> match = MainWindow.this.matchModel.getMatch(MainWindow.this.selectionTable.getSelectedRow());
				if (match.isPresent()) {
					Main.getMainWindow().getEternalMapModel().wireUp(Main.getWrapper(), match.get().getCenterMap());
					Main.getMainWindow().getGreenMapModel().wireUp(Main.getWrapper(), match.get().getGreenMap());
					Main.getMainWindow().getBlueMapModel().wireUp(Main.getWrapper(), match.get().getBlueMap());
					Main.getMainWindow().getRedMapModel().wireUp(Main.getWrapper(), match.get().getRedMap());
					Main.getMainWindow().getTabPane().setTitleAt(2,match.get().getGreenWorld().getName().get()+" (grüne Grenzlande)");
					Main.getMainWindow().getTabPane().setTitleAt(3,match.get().getBlueWorld().getName().get()+" (blaue Grenzlande)");
					Main.getMainWindow().getTabPane().setTitleAt(4,match.get().getRedWorld().getName().get()+" (rote Grenzlande)");
				}

			}
		});

		selectionPanel.add(selectionButton, BorderLayout.SOUTH);

		return selectionPanel;
	}

	private JPanel initEternalPanel() {
		final JPanel eternalPanel = new JPanel();
		eternalPanel.setLayout(new BorderLayout());
		eternalPanel.add(new JLabel("Punkte Ewige Schlachtfelder"), BorderLayout.NORTH);

		this.eternalTable = new JTable(this.eternalMapModel, this.newMapTCM());
		
		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(this.eternalMapModel);
		this.eternalTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for(int col = 0; col < this.eternalMapModel.getColumnCount(); col++) {
			sorter.setComparator(col, this.eternalMapModel.getColumnComparator(col));
		}
		
		eternalPanel.add(new JScrollPane(this.eternalTable), BorderLayout.CENTER);
		return eternalPanel;
	}

	private JPanel initGreenPanel() {
		final JPanel greenPanel = new JPanel();
		greenPanel.setLayout(new BorderLayout());
		greenPanel.add(new JLabel("Punkte Grüne Grenzlande"), BorderLayout.NORTH);

		this.greenTable = new JTable(this.greenMapModel, this.newMapTCM());
		
		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(this.greenMapModel);
		this.greenTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for(int col = 0; col < this.greenMapModel.getColumnCount(); col++) {
			sorter.setComparator(col, this.greenMapModel.getColumnComparator(col));
		}
		
		greenPanel.add(new JScrollPane(this.greenTable), BorderLayout.CENTER);
		return greenPanel;
	}

	private JPanel initBluePanel() {
		final JPanel bluePanel = new JPanel();
		bluePanel.setLayout(new BorderLayout());
		bluePanel.add(new JLabel("Punkte Blaue Grenzlande"), BorderLayout.NORTH);

		this.blueTable = new JTable(this.blueMapModel, this.newMapTCM());
		
		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(this.blueMapModel);
		this.blueTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for(int col = 0; col < this.blueMapModel.getColumnCount(); col++) {
			sorter.setComparator(col, this.blueMapModel.getColumnComparator(col));
		}
		
		bluePanel.add(new JScrollPane(this.blueTable), BorderLayout.CENTER);
		return bluePanel;
	}

	private JPanel initRedPanel() {
		final JPanel redPanel = new JPanel();
		redPanel.setLayout(new BorderLayout());
		redPanel.add(new JLabel("Punkte Rote Grenzlande"), BorderLayout.NORTH);

		this.redTable = new JTable(this.redMapModel, this.newMapTCM());
		
		final TableRowSorter<MapObjectivesTableModel> sorter = new TableRowSorter<MapObjectivesTableModel>(this.redMapModel);
		this.redTable.setRowSorter(sorter);
		sorter.setSortsOnUpdates(true);
		for(int col = 0; col < this.redMapModel.getColumnCount(); col++) {
			sorter.setComparator(col, this.redMapModel.getColumnComparator(col));
		}
		
		redPanel.add(new JScrollPane(this.redTable), BorderLayout.CENTER);
		return redPanel;
	}
	
	private JPanel initGraphicMapTestPanel() {
		final JPanel graphicMapTestPanel = new JPanel();
		graphicMapTestPanel.setLayout(new BorderLayout());
		
		final ListenableUndirectedWeightedGraph<String, DefaultEdge> graph = new ListenableUndirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
		graph.addVertex("TEST");
		graph.addVertex("TEST1");
		graph.addVertex("TEST2");
		graph.addVertex("TEST3");
		graph.addEdge("TEST", "TEST1");
		graph.addEdge("TEST", "TEST2");
		graph.addEdge("TEST", "TEST3");
		graph.addEdge("TEST3", "TEST2");
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
}
