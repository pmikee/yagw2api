package de.justi.yagw2api.core.wrapper.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import org.apache.log4j.Logger;

abstract class AbstractSynchronizerAction<T, A extends AbstractSynchronizerAction<T, ?>> extends RecursiveAction{
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
		if(LOGGER.isTraceEnabled()) {
			LOGGER.trace("New " + this.getClass().getSimpleName() + " that handles content " + this.fromInclusive +" - "+this.toExclusive);
		}
	}
	
	@Override
	protected final void compute() {
		final long startTimestamp = System.currentTimeMillis();
		try {
			if (this.toExclusive - this.fromInclusive <= this.chunkSize) {
				// compute directly
				for (int index = this.fromInclusive; index < this.toExclusive; index++) {
					this.perform(this.content.get(index));
				}
			} else {
				// fork
				final int splitAtIndex = this.fromInclusive + ((this.toExclusive - this.fromInclusive) / 2);
				invokeAll(this.createSubTask(this.content, this.chunkSize, this.fromInclusive, splitAtIndex), this.createSubTask(this.content, this.chunkSize, splitAtIndex,
						this.toExclusive));
			}
		} catch (Exception e) {
			LOGGER.fatal("Failed during execution of "+this.getClass().getSimpleName()+" computing "+this.fromInclusive+"-"+this.toExclusive,e);
		}
		final long endTimestamp = System.currentTimeMillis();
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Computed " + this.getClass().getSimpleName() + " that handled content " + this.fromInclusive +" - "+this.toExclusive+ " in "+(endTimestamp-startTimestamp)+"ms");
		}
	}

	protected abstract A createSubTask(List<T> content, int chunkSize, int fromInclusive, int toExclusive);
	protected abstract void perform(T content);
}
