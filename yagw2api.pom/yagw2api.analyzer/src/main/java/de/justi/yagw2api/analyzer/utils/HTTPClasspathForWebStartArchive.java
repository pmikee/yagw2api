package de.justi.yagw2api.analyzer.utils;

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

import org.eclipse.persistence.internal.jpa.deployment.ArchiveBase;
import org.eclipse.persistence.jpa.Archive;

public class HTTPClasspathForWebStartArchive extends ArchiveBase implements Archive {
	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(HTTPClasspathForWebStartArchive.class);
	private static final String[] POSSIBLE_ENTRIES = new String[] { "META-INF/persistence.xml" };
	
	private final URL url;
	private final ClassLoader loader;

	
	private final List<String> entries = new ArrayList<String>();
	private Logger logger;

	public HTTPClasspathForWebStartArchive(URL url, Logger logger, String descriptorLocation) throws IOException {
		super(checkNotNull(url), checkNotNull(descriptorLocation));
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
