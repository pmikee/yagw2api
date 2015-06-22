package de.justi.yagw2api.wrapper;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Wrapper
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
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;

public abstract class AbstractSynchronizerAction<T, A extends AbstractSynchronizerAction<T, ?>> extends RecursiveAction {
	private static final long serialVersionUID = -2838650978601355556L;
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSynchronizerAction.class);

	private final int chunkSize;
	private final List<T> content;
	private final int fromInclusive;
	private final int toExclusive;

	protected AbstractSynchronizerAction(final List<T> content, final int chunkSize, final int fromInclusive, final int toExclusive) {
		checkArgument(chunkSize > 0);
		checkNotNull(content);
		checkArgument(fromInclusive >= 0);
		checkArgument(fromInclusive <= toExclusive);
		this.chunkSize = chunkSize;
		this.content = content;
		this.fromInclusive = fromInclusive;
		this.toExclusive = toExclusive;
		LOGGER.trace("New {} that handels content {}-{}", this.getClass(), this.fromInclusive, this.toExclusive);
	}

	@Override
	protected final void compute() {
		final long startTimestamp = System.currentTimeMillis();

		if ((this.toExclusive - this.fromInclusive) <= this.chunkSize) {
			try {
				// compute directly
				LOGGER.debug("{} is going to perform on {}-{}", this, this.fromInclusive, this.toExclusive);

				for (int index = this.fromInclusive; index < this.toExclusive; index++) {
					final T content = this.content.get(index);
					LOGGER.trace("{} is going to perform on {} -> {}", this, index, content);
					this.perform(content);
				}
			} catch (final Throwable t) {
				LOGGER.error("{} failed", t);
			}
		} else {
			// fork
			final int splitAtIndex = this.fromInclusive + ((this.toExclusive - this.fromInclusive) / 2);
			invokeAll(this.createSubTask(this.content, this.chunkSize, this.fromInclusive, splitAtIndex), this.createSubTask(this.content, this.chunkSize, splitAtIndex, this.toExclusive));
		}
		final long endTimestamp = System.currentTimeMillis();
		if (LOGGER.isDebugEnabled()) {

		}
		LOGGER.debug("{} handled content {}-{} in {}ms", this, this.fromInclusive, this.toExclusive, (endTimestamp - startTimestamp));
	}

	protected ToStringHelper toStringHelper() {
		return MoreObjects.toStringHelper(this).add("chunkSize", this.chunkSize).add("from", this.fromInclusive).add("to", this.toExclusive).add("content", this.content);
	}

	@Override
	public final String toString() {
		return this.toStringHelper().toString();
	}

	protected abstract A createSubTask(List<T> content, int chunkSize, int fromInclusive, int toExclusive);

	protected abstract void perform(T content);
}
