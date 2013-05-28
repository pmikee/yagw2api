package de.justi.yagw2api.wrapper.model;


import com.google.common.eventbus.EventBus;


public abstract class AbstractHasChannel implements IHasChannel {
	private final EventBus channel = new EventBus(this.getChannelName());
	public final EventBus getChannel() {
		return this.channel;
	}
	
	/**
	 * override to modify channel name
	 * @return
	 */
	protected String getChannelName(){
		return this.getClass().getName();
	}
}
