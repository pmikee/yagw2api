package de.justi.yagw2api.wrapper.impl;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
 * -------------------------------------------------------------
 * Copyright (C) 2012 - 2013 Julian Stitz
 * -------------------------------------------------------------
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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.apache.log4j.Logger;

abstract class AbstractSynchronizerAction<T, A extends AbstractSynchronizerAction<T, ?>> extends RecursiveAction {
	private static final long serialVersionUID = -2838650978601355556L;
	private static final Logger LOGGER = Logger.getLogger(AbstractSynchronizerAction.class);

	private final int chunkSize;
	private final List<T> content;
	private final int fromInclusive;
	private final int toExclusive;

	protected AbstractSynchronizerAction(List<T> content, int chunkSize, int fromInclusive, int toExclusive) {
		checkArgument(chunkSize > 0);
		checkNotNull(content);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		this.chunkSize = chunkSize;
		this.content = content;
		this.fromInclusive = fromInclusive;
		this.toExclusive = toExclusive;
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("New " + this.getClass().getSimpleName() + " that handles content " + this.fromInclusive + " - " + this.toExclusive);
		}
	}

	@Override
	protected final void compute() {
		final long startTimestamp = System.currentTimeMillis();

		if ((this.toExclusive - this.fromInclusive) <= this.chunkSize) {
			try {
				// compute directly
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(this.getClass().getSimpleName() + " is going to perform on " + this.fromInclusive + " to " + this.toExclusive);
				}
				T content = null;
				for (int index = this.fromInclusive; index < this.toExclusive; index++) {
					content = this.content.get(index);
					if (LOGGER.isTraceEnabled()) {
						LOGGER.trace(this.getClass().getSimpleName() + " is going to perform on " + index + " -> " + content);
					}
					this.perform(content);
				}
			} catch (Throwable t) {
				LOGGER.error("Failed during execution of " + this.getClass().getSimpleName() + " computing " + this.fromInclusive + "-" + this.toExclusive, t);
			}
		} else {
			// fork
			final int splitAtIndex = this.fromInclusive + ((this.toExclusive - this.fromInclusive) / 2);
			invokeAll(this.createSubTask(this.content, this.chunkSize, this.fromInclusive, splitAtIndex), this.createSubTask(this.content, this.chunkSize, splitAtIndex, this.toExclusive));
		}
		final long endTimestamp = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Computed " + this.getClass().getSimpleName() + " that handled content " + this.fromInclusive + " - " + this.toExclusive + " in " + (endTimestamp - startTimestamp) + "ms");
		}
	}

	protected abstract A createSubTask(List<T> content, int chunkSize, int fromInclusive, int toExclusive);

	protected abstract void perform(T content);
}
