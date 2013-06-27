package de.justi.yagw2api.analyzer.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.eclipse.persistence.jpa.Archive;
import org.eclipse.persistence.jpa.ArchiveFactory;

public final class ArchiveFactoryImpl extends org.eclipse.persistence.internal.jpa.deployment.ArchiveFactoryImpl implements ArchiveFactory {
	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ArchiveFactoryImpl.class);

	@Override
	public Archive createArchive(URL rootUrl, String descriptorLocation, @SuppressWarnings("rawtypes") Map properties) throws URISyntaxException, IOException {
		if (rootUrl == null) {
			return null;
		} else {
			Archive result = null;
			String protocol = rootUrl.getProtocol();
			if (protocol != null && protocol.startsWith("http") && rootUrl.toExternalForm().endsWith(".jar") && getClass().getClassLoader().getClass().getName().contains("JNLP")
					&& descriptorLocation != null) {
				LOGGER.warn("Injecting " + HTTPClasspathForWebStartArchive.class.getName() + " for rootUrl=" + rootUrl + " and descriptorLocation=" + descriptorLocation);
				result = new HTTPClasspathForWebStartArchive(rootUrl, this.logger, descriptorLocation);
			} else {
				result = super.createArchive(rootUrl, descriptorLocation, properties);
			}
			return result;
		}
	}
}
