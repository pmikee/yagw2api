package de.justi.yagw2api.explorer.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

abstract class AbstractWindow extends JFrame {
	private static final long serialVersionUID = -4498743801575544773L;
	private final JPanel contentPanel;

	public AbstractWindow() {
		this.contentPanel = new JPanel(true);
		this.contentPanel.setLayout(new BorderLayout());
		this.setContentPane(this.contentPanel);
	}
	
	public final JPanel getContentPanel() {
		return this.contentPanel;
	}
}
