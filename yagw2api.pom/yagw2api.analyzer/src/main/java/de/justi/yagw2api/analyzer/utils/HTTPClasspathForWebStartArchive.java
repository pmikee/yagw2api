package de.justi.yagw2api.analyzer.utils;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.internal.jpa.deployment.ArchiveBase;
import org.eclipse.persistence.jpa.Archive;
import org.slf4j.LoggerFactory;

public final class HTTPClasspathForWebStartArchive extends ArchiveBase implements Archive {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HTTPClasspathForWebStartArchive.class);
	private static final String[] POSSIBLE_ENTRIES = new String[] { PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML_DEFAULT };
	
	private final URL url;
	private final ClassLoader loader;

	
	private final List<String> entries = new ArrayList<String>();
	private Logger logger;

	public HTTPClasspathForWebStartArchive(URL url, Logger logger, String descriptorLocation) throws IOException {
		super(checkNotNull(url,"URL should not be null."), checkNotNull(descriptorLocation, "DescriptorLocation should not be null"));
		this.logger = checkNotNull(logger);
		this.logger.entering("HTTPClasspathForWebStartArchive", "HTTPClasspathForWebStartArchive", // NOI18N
				new Object[] { url });		
		this.url = checkNotNull(url);
		this.loader = checkNotNull(this.getClass().getClassLoader());
		init();
		checkState(this.entries.size() > 0);
		LOGGER.info("Initialized new "+this.getClass().getSimpleName()+" for "+url+": "+this.entries);
	}

	private void init() throws IOException {
		InputStream is = null;
		for (String entry : POSSIBLE_ENTRIES) {
			try {
				is = this.loader.getResourceAsStream(entry);
				if (is != null) {
					this.entries.add(entry);
					LOGGER.info("Successfully located entry="+entry+" using "+this.loader);
				}else {
					LOGGER.warn("Unable to locate entry="+entry+" using "+this.loader);
				}
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
					is = null;
				}
			}
		}
		LOGGER.info("Initiailzed " + this + ": " + entries);
	}

	public Iterator<String> getEntries() {
		return this.entries.iterator();
	}

	public InputStream getEntry(String entryPath) throws IOException {
		LOGGER.info("Going to return " + InputStream.class.getSimpleName() + " for " + entryPath);
		if (!this.entries.contains(entryPath)) {
			return null;
		}
		InputStream is = null;
		is = this.loader.getResourceAsStream(entryPath);
		return (is == null) ? null : new BufferedInputStream(is);
	}

	public URL getEntryAsURL(String entryPath) throws IOException {
		URL result = this.entries.contains(entryPath) ? result = new URL("jar:" + this.url + "!/" + entryPath) : null; // NOI18N
		return result;
	}

	@Override
	public void close() {
	}
}
