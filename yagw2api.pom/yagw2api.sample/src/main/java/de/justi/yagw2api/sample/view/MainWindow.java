package de.justi.yagw2api.sample.view;

import java.awt.Dimension;


public class MainWindow extends AbstractWindow {
	private static final long serialVersionUID = -6500541020042114865L;

	public MainWindow() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(640, 480));
		this.setPreferredSize(new Dimension(1024, 768));
		this.pack();
	}
}
