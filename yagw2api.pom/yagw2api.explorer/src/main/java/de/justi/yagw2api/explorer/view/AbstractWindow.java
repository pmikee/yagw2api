package de.justi.yagw2api.explorer.view;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-Application
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


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
