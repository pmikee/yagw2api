package de.justi.yagw2api.common.rest;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Commons
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

import static com.google.common.base.Preconditions.checkArgument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public final class RetryClientFilter extends ClientFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(RetryClientFilter.class);
	private final int maximumRetryCount;

	public RetryClientFilter(final int maximumRetryCount) {
		checkArgument(maximumRetryCount >= 0);
		this.maximumRetryCount = maximumRetryCount;
	}

	@Override
	public final ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {

		int i = 0;

		while (i <= this.maximumRetryCount) {
			if (i++ > 0) {
				this.onRetry(i - 1);
			}
			try {
				return this.getNext().handle(cr);
			} catch (ClientHandlerException e) {
				LOGGER.warn("Exception thrown while quering " + cr.getURI(), e);
			}
		}
		throw new ClientHandlerException("Connection retries limit of " + this.maximumRetryCount + " exceeded.");
	}

	protected void onRetry(final int retry) {
		LOGGER.warn("Will now perform retry " + retry + "/" + this.maximumRetryCount);
	}

}
