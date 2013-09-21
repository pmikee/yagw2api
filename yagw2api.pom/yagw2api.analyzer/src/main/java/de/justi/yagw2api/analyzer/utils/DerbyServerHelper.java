package de.justi.yagw2api.analyzer.utils;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
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


import java.io.PrintWriter;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.justi.yagwapi.common.LogOutputStream;

public final class DerbyServerHelper extends Thread implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(DerbyServerHelper.class);

	public DerbyServerHelper() {

	}

	@Override
	public void run() {
		NetworkServerControl server;
		try {
			server = new NetworkServerControl();
			server.start(new PrintWriter(new LogOutputStream(LOGGER, Level.INFO)));
		} catch (Exception e) {
			LOGGER.error("Exception thrown while starting server.", e);
		}
	}
}
