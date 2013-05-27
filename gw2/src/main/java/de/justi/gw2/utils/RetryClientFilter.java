package de.justi.gw2.utils;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class RetryClientFilter extends ClientFilter {
	private static final Logger	LOGGER	= Logger.getLogger(RetryClientFilter.class);
	private final int			maximumRetryCount;

	public RetryClientFilter(int maximumRetryCount) {
		checkArgument(maximumRetryCount >= 0);
		this.maximumRetryCount = maximumRetryCount;
	}

	@Override
	public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {

		int i = 0;

		while (i <= this.maximumRetryCount) {
			if(i++ > 0){
				LOGGER.warn("Will now perform retry " + i+"/"+this.maximumRetryCount);
			}
			try {
				return this.getNext().handle(cr);
			} catch (ClientHandlerException e) {
				LOGGER.warn("Exception thrown while quering " + cr.getURI(), e);
			}
		}
		throw new ClientHandlerException("Connection retries limit of " + this.maximumRetryCount + " exceeded.");
	}

}
