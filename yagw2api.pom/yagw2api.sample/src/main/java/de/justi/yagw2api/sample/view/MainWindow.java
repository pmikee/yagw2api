package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.google.common.base.Optional;

import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMap;
import de.justi.yagw2api.core.wrapper.model.wvw.IWVWMatch;
import de.justi.yagw2api.sample.Main;
import de.justi.yagw2api.sample.model.MapObjectivesTableModel;
import de.justi.yagw2api.sample.model.MatchesTableModel;

public class MainWindow extends AbstractWindow {
	private static final long				serialVersionUID	= -6500541020042114865L;

	private final MatchesTableModel			matchModel;
	private final MapObjectivesTableModel	eternalMapModel;
	private final MapObjectivesTableModel	greenMapModel;
	private final MapObjectivesTableModel	blueMapModel;
	private final MapObjectivesTableModel	redMapModel;

	private JTable							selectionTable;
	private JTable							eternalTable;
	private JTable							greenTable;
	private JTable							blueTable;
	private JTable							redTable;

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
		final JPanel eternalPanel = this.initEternalPanel();
		final JPanel greenPanel = this.initGreenPanel();
		final JPanel bluePanel = this.initBluePanel();
		final JPanel redPanel = this.initRedPanel();

		final JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Matches", selectionPanel);
		tabPane.addTab("Ewige", eternalPanel);
		tabPane.addTab("Grün", greenPanel);
		tabPane.addTab("Blau", bluePanel);
		tabPane.addTab("Rot", redPanel);

		this.getContentPanel().add(tabPane, BorderLayout.CENTER);

		this.pack();
	}

	private TableColumnModel newMapTCM() {

		final TableColumnModel tcm = new DefaultTableColumnModel();
		final TableColumn c1 = new TableColumn(0);
		c1.setHeaderValue("Objekt");
		tcm.addColumn(c1);
		final TableColumn c2 = new TableColumn(1);
		c2.setHeaderValue("Objekttyp");
		tcm.addColumn(c2);
		final TableColumn c3 = new TableColumn(2);
		c3.setHeaderValue("Besitzer");
		tcm.addColumn(c3);
		final TableColumn c4 = new TableColumn(3);
		c4.setHeaderValue("Buff");
		tcm.addColumn(c4);

		return tcm;
	}

	private JPanel initSelectionPanel() {
		final JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());
		selectionPanel.add(new JLabel("Übersicht und Auswahl des Matches"), BorderLayout.NORTH);

		final TableColumnModel tcm = new DefaultTableColumnModel();
		final TableColumn c1 = new TableColumn(0);
		c1.setHeaderValue("Grün");
		tcm.addColumn(c1);
		final TableColumn c2 = new TableColumn(1);
		c2.setHeaderValue("Punkte (Grün)");
		tcm.addColumn(c2);
		final TableColumn c3 = new TableColumn(2);
		c3.setHeaderValue("Blau");
		tcm.addColumn(c3);
		final TableColumn c4 = new TableColumn(3);
		c4.setHeaderValue("Punkte (Blau)");
		tcm.addColumn(c4);
		final TableColumn c5 = new TableColumn(4);
		c5.setHeaderValue("Rot");
		tcm.addColumn(c5);
		final TableColumn c6 = new TableColumn(5);
		c6.setHeaderValue("Punkte (Rot)");
		tcm.addColumn(c6);

		this.selectionTable = new JTable(this.matchModel, tcm);
		this.selectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectionPanel.add(new JScrollPane(this.selectionTable), BorderLayout.CENTER);

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

		eternalTable = new JTable(this.eternalMapModel, this.newMapTCM());
		eternalPanel.add(new JScrollPane(this.eternalTable), BorderLayout.CENTER);
		return eternalPanel;
	}

	private JPanel initGreenPanel() {
		final JPanel greenPanel = new JPanel();
		greenPanel.setLayout(new BorderLayout());
		greenPanel.add(new JLabel("Punkte Grüne Grenzlande"), BorderLayout.NORTH);

		greenTable = new JTable(this.greenMapModel, this.newMapTCM());
		greenPanel.add(new JScrollPane(this.greenTable), BorderLayout.CENTER);
		return greenPanel;
	}

	private JPanel initBluePanel() {
		final JPanel bluePanel = new JPanel();
		bluePanel.setLayout(new BorderLayout());
		bluePanel.add(new JLabel("Punkte Blaue Grenzlande"), BorderLayout.NORTH);

		blueTable = new JTable(this.blueMapModel, this.newMapTCM());
		bluePanel.add(new JScrollPane(this.blueTable), BorderLayout.CENTER);
		return bluePanel;
	}

	private JPanel initRedPanel() {
		final JPanel redPanel = new JPanel();
		redPanel.setLayout(new BorderLayout());
		redPanel.add(new JLabel("Punkte Rote Grenzlande"), BorderLayout.NORTH);

		redTable = new JTable(this.redMapModel, this.newMapTCM());
		redPanel.add(new JScrollPane(this.redTable), BorderLayout.CENTER);
		return redPanel;
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
}
