package de.justi.yagw2api.explorer.rcp;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Explorer-RCP-Application
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import java.util.Locale;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.BundleContext;

import de.justi.yagw2api.arenanet.MapTileService;
import de.justi.yagw2api.arenanet.YAGW2APIArenanet;
import de.justi.yagw2api.wrapper.YAGW2APIWrapper;
import de.justi.yagw2api.wrapper.wvw.WVWWrapper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	// The plug-in ID
	public static final String PLUGIN_ID = "yagw2api.explorer.rcp"; //$NON-NLS-1$

	static {
		// ensure that SWTRessourceManager becomes disposed using a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread("YAGW2API-SHUTDOWN-HOOK") {
			@Override
			public void run() {
				SWTResourceManager.dispose();
			}
		});

		YAGW2APIArenanet.INSTANCE.setCurrentLocale(Locale.getDefault());
	}

	// The shared instance
	private static Activator plugin;

	private final MapTileService mapTileService = YAGW2APIArenanet.INSTANCE.getMapTileService();
	private final WVWWrapper wvw = YAGW2APIWrapper.INSTANCE.getWVWWrapper();

	/**
	 * The constructor
	 */
	public Activator() {
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		this.wvw.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		this.wvw.stop();
	}

	public WVWWrapper getWVW() {
		return this.wvw;
	}

	public MapTileService getMapTileService() {
		return this.mapTileService;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
