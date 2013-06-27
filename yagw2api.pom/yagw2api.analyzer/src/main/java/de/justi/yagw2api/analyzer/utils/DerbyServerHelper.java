package de.justi.yagw2api.analyzer.utils;

import org.apache.derby.drda.NetworkServerControl;
import org.apache.log4j.Logger;

public final class DerbyServerHelper extends Thread implements Runnable{
	private static final Logger LOGGER = Logger.getLogger(DerbyServerHelper.class);
	public DerbyServerHelper() {
		
	}

	@Override
	public void run() {
		NetworkServerControl server;
		try {
			server = new NetworkServerControl();
			server.start(null);
		} catch (Exception e) {
			LOGGER.error("Exception thrown while starting server.",e);
		}
	}
	
}
