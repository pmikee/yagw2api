/*******************************************************************************
 * Copyright (c) 1998, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/
package de.justi.yagw2api.analyzer.utils;

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

/**
 * This is an implementation of {@link Archive} which is used when container
 * returns some form of URL from which an InputStream in jar format can be
 * obtained. e.g. jar:file:/tmp/a_ear/b.war!/WEB-INF/lib/pu.jar
 *
 * @author Sanjeeb.Sahoo@Sun.COM
 */
public class HTTPClasspathForWebStartArchive extends ArchiveBase implements Archive {
    private URL url;
    private ClassLoader loader;
    
    private List<String> entries = new ArrayList<String>();

    private String[] possibleEntries = new String[]{"META-INF/persistence.xml"};
    
    
    @SuppressWarnings("unused")
    private Logger logger;

    @SuppressWarnings("deprecation")
    public HTTPClasspathForWebStartArchive(URL url) throws IOException {
        this(url, Logger.global);
    }

    public HTTPClasspathForWebStartArchive(URL url, Logger logger) throws IOException {
        logger.entering("HTTPClasspathForWebStartArchive", "HTTPClasspathForWebStartArchive", // NOI18N
                new Object[]{url});
        this.logger = logger;
        this.url = url;
        loader = getClass().getClassLoader();
        init();
    }

    private void init() throws IOException {
        InputStream is = null;
        for (String entry : possibleEntries) {
            try {
                is = loader.getResourceAsStream(entry);
                if (is != null) {
                    entries.add(entry);
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
    }

    public Iterator<String> getEntries() {
        return entries.iterator();
    }

    public InputStream getEntry(String entryPath) throws IOException {
        if (!entries.contains(entryPath)) {
            return null;
        }
        InputStream is = null;
        is =loader.getResourceAsStream(entryPath);
        return (is == null) ? null : new BufferedInputStream(is); 
    }

    public URL getEntryAsURL(String entryPath) throws IOException {
        URL result = entries.contains(entryPath) ?
            result = new URL("jar:"+url+"!/"+entryPath) : null; // NOI18N
        return result;
    }

    public URL getRootURL() {
        return url;
    }

	@Override
	public void close() {	
	}
}
