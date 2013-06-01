package de.justi.yagw2api.analyzer.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.eclipse.persistence.internal.helper.Helper;
import org.eclipse.persistence.internal.jpa.deployment.DirectoryArchive;
import org.eclipse.persistence.internal.jpa.deployment.DirectoryInsideJarURLArchive;
import org.eclipse.persistence.internal.jpa.deployment.JarFileArchive;
import org.eclipse.persistence.internal.jpa.deployment.JarInputStreamURLArchive;
import org.eclipse.persistence.internal.jpa.deployment.URLArchive;
import org.eclipse.persistence.jpa.Archive;
import org.eclipse.persistence.jpa.ArchiveFactory;

public class ArchiveFactoryImpl extends org.eclipse.persistence.internal.jpa.deployment.ArchiveFactoryImpl implements ArchiveFactory {
	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ArchiveFactoryImpl.class);
	@Override
	public Archive createArchive(URL rootUrl, String descriptorLocation, @SuppressWarnings("rawtypes") Map properties) throws URISyntaxException, IOException {
		logger.entering("ArchiveFactoryImpl", "createArchive", new Object[] { rootUrl, descriptorLocation });

		if (rootUrl == null) {
			return null;
		}

		Archive result = null;
		String protocol = rootUrl.getProtocol();
		logger.logp(Level.FINER, "ArchiveFactoryImpl", "createArchive", "protocol = {0}", protocol);

		if ("file".equals(protocol)) {
			URI uri = Helper.toURI(rootUrl);

			File f;
			try {
				// Attempt to create the file with the uri. The pre-conditions
				// are checked in the constructor and an exception is thrown
				// if the uri does not meet them.
				f = new File(uri);
			} catch (IllegalArgumentException e) {
				// Invalid uri for File. Go our back up route of using the
				// path from the url.
				f = new File(rootUrl.getPath());
			}

			if (f.isDirectory()) {
				// e.g. file:/tmp/a_ear/ejb_jar
				result = new DirectoryArchive(f, descriptorLocation);
			} else {
				// e.g. file:/tmp/a_ear/lib/pu.jarlo
				// It's not a directory. Then it must be a jar file.
				result = new JarFileArchive(rootUrl, new JarFile(f), descriptorLocation);
			}
		} else if ("jar".equals(protocol)) { // NOI18N
			JarURLConnection conn = JarURLConnection.class.cast(rootUrl.openConnection());
			conn.setUseCaches(false);
			JarEntry je = conn.getJarEntry();
			if (je == null) {
				// e.g. jar:file:/tmp/a_ear/lib/pu.jar!/
				// No entryName specified, hence URL points to a JAR file and
				// not to any entry inside it. Ideally this should have been
				// file:/tmp/a_ear/lib/pu.jar,
				// but containers (e.g.) WebLogic return this kind of URL,
				// so we better handle this in our code to improve pluggability.
				// Read the entire jar file.
				result = new JarFileArchive(rootUrl, conn.getJarFile(), descriptorLocation);
			} else if (je.isDirectory()) {
				// e.g. jar:file:/tmp/a_ear/b.war!/WEB-INF/classes/
				// entryName [je.getName()] is a directory
				result = new DirectoryInsideJarURLArchive(rootUrl, descriptorLocation);
			} else {
				// some URL (e.g.) jar:file:/tmp/a_ear/b.war!/WEB-INF/lib/pu.jar
				// entryName [je.getName()] is a file, so treat this URL as a
				// URL from which a JAR format InputStream can be obtained.
				result = new JarInputStreamURLArchive(rootUrl, descriptorLocation);
			}
			// Following three lines added by CHope to handle url with protocol
			// http (Java Web start)
			// Without this, will not run "offline" because tries to open
			// connection to http url
			// whereas needs to load Resource from jar using JNLP classloader -
			// code in Archive class.
		} else if ("http".equals(protocol) && rootUrl.toExternalForm().endsWith(".jar") && getClass().getClassLoader().getClass().getName().indexOf("JNLP") != -1) {
			LOGGER.warn("Injecting "+HTTPClasspathForWebStartArchive.class.getName()+" for rootUrl="+rootUrl+" and descriptorLocation="+descriptorLocation);
			result = new HTTPClasspathForWebStartArchive(rootUrl,this.logger,descriptorLocation);
		} else if (isJarInputStream(rootUrl)) {
			result = new JarInputStreamURLArchive(rootUrl, descriptorLocation);
		} else {
			result = new URLArchive(rootUrl, descriptorLocation);
		}
		logger.exiting("ArchiveFactoryImpl", "createArchive", result);
		return result;
	}
}
