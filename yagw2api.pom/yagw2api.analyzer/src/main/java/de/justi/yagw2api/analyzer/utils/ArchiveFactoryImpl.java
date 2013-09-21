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
