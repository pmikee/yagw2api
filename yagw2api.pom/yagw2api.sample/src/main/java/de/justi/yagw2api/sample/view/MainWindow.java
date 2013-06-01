package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import de.justi.yagw2api.sample.model.MatchesTableModel;


public class MainWindow extends AbstractWindow {
	private static final long serialVersionUID = -6500541020042114865L;
	
	private final MatchesTableModel model;
	
	private JTable selectionTable;

	public MainWindow() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(1024, 768));
		
		this.model = new MatchesTableModel();
		

		final JPanel selectionPanel = this.initSelectionPanel();
		final JPanel eternalPanel = this.initEternalPanel();
		final JPanel greenPanel = this.initGreenPanel();
		final JPanel bluePanel = this.initBluePanel();
		final JPanel redPanel = this.initRedPanel();
		
		final JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Auswahl", selectionPanel);
		tabPane.addTab("Ewige", eternalPanel);
		tabPane.addTab("Grün", greenPanel);
		tabPane.addTab("Blau", bluePanel);
		tabPane.addTab("Rot", redPanel);
		
		this.getContentPanel().add(tabPane, BorderLayout.CENTER);
		
		this.pack();
	}

	private JPanel initSelectionPanel() {
		final JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BorderLayout());
		selectionPanel.add(new JLabel("Auswahl des Matches"), BorderLayout.NORTH);
		
		selectionTable = new JTable(this.model);
		selectionPanel.add(selectionTable, BorderLayout.CENTER);
		
		return selectionPanel;
	}
	
	private JPanel initEternalPanel() {
		final JPanel eternalPanel = new JPanel();
		eternalPanel.add(new JLabel("Punkte Ewige Schlachtfelder"), BorderLayout.NORTH);
		
		return eternalPanel;
	}
	
	private JPanel initGreenPanel() {
		final JPanel greenPanel = new JPanel();
		greenPanel.add(new JLabel("Punkte Grüne Grenzländer"), BorderLayout.NORTH);
		
		return greenPanel;
	}
	
	private JPanel initBluePanel() {
		final JPanel bluePanel = new JPanel();
		bluePanel.add(new JLabel("Punkte Blaue Grenzländer"), BorderLayout.NORTH);
		
		return bluePanel;
	}

	private JPanel initRedPanel() {
		final JPanel redPanel = new JPanel();
		redPanel.add(new JLabel("Punkte Rote Grenzländer"), BorderLayout.NORTH);
		
		return redPanel;
	}


	public MatchesTableModel getModel() {
		return this.model;
	}
}
