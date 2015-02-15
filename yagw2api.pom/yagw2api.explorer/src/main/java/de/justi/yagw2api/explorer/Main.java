package de.justi.yagw2api.explorer;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-Application
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
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

import java.io.IOException;
import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzer;
import de.justi.yagw2api.analyzer.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.anchorman.IAnchorman;
import de.justi.yagw2api.anchorman.YAGW2APIAnchorman;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.explorer.view.MainWindow;
import de.justi.yagw2api.mumblelink.YAGW2APIMumbleLink;
import de.justi.yagw2api.wrapper.IWVWWrapper;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;

public final class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	static {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

	}

	/**
	 * @param args
	 * @throws MaryConfigurationException
	 * @throws SynthesisException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		YAGW2APIArenanet.INSTANCE.setCurrentLocale(Locale.getDefault());
		final IWVWWrapper apiWrapper = YAGW2APIWrapper.INSTANCE.getWVWWrapper();
		final IWVWAnalyzer analyzer = YAGW2APIAnalyzer.getAnalyzer();
		YAGW2APIAnalyzerPersistence.getDefaultEM(); // startup db connection
		final MainWindow mainWindow = new MainWindow();
		mainWindow.setVisible(true);

		// start the api wrapper
		apiWrapper.start();
		mainWindow.wireUp(apiWrapper);

		// wire everything up
		apiWrapper.registerWVWMapListener(analyzer);
		apiWrapper.registerWVWMatchListener(analyzer);

		final IAnchorman anchorman = YAGW2APIAnchorman.INSTANCE.getAnchorman();
		anchorman.init(apiWrapper, YAGW2APIMumbleLink.INSTANCE.getMumbleLink());
		anchorman.start();

	}
}
