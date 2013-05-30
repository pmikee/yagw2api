package de.justi.yagw2api.sample.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTable;

import de.justi.yagw2api.sample.model.MapObjectivesTableModel;


public class MainWindow extends AbstractWindow {
	private static final long serialVersionUID = -6500541020042114865L;
	
	private MapObjectivesTableModel model;

	public MainWindow() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(1024, 768));
		
		this.model = new MapObjectivesTableModel();
		final JTable table = new JTable(this.model);
		
		
		
		this.getContentPanel().add(table, BorderLayout.CENTER);
		
		this.pack();
	}
	
	public MapObjectivesTableModel getModel() {
		return this.model;
	}
}
