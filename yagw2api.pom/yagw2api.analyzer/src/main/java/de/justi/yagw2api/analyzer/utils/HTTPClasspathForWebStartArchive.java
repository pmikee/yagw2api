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

public class HTTPClasspathForWebStartArchive extends ArchiveBase implements Archive {
	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(HTTPClasspathForWebStartArchive.class);
    private URL url;
    private ClassLoader loader;
    
    private List<String> entries = new ArrayList<String>();

    private String[] possibleEntries = new String[]{"META-INF/persistence.xml"};
    
    
    @SuppressWarnings("unused")
    private Logger logger;

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
        LOGGER.info("Initiailzed "+this+": "+entries);
    }

    public Iterator<String> getEntries() {
        return entries.iterator();
    }

    public InputStream getEntry(String entryPath) throws IOException {
    	LOGGER.info("Going to return "+InputStream.class.getSimpleName()+" for "+entryPath);
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

	@Override
	public void close() {	
	}
}
