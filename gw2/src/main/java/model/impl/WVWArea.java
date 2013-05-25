package model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import model.IWVWArea;
import model.IWVWMap;
import model.IWVWMapType;


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

class WVWArea extends AbstractHasChannel implements IWVWArea {
	private static final IWVWMapType CENTER_TYPE = WVWMapType.CENTER;
	private static final IWVWMapType RED_TYPE = WVWMapType.RED;
	private static final IWVWMapType GREEN_TYPE = WVWMapType.GREEN;
	private static final IWVWMapType BLUE_TYPE = WVWMapType.BLUE;
	static {
		checkState(CENTER_TYPE.isCenter());
		checkState(RED_TYPE.isCenter());
		checkState(GREEN_TYPE.isCenter());
		checkState(BLUE_TYPE.isCenter());
	}
	
	private final Map<IWVWMapType, IWVWMap> maps;
	
	public WVWArea(IWVWMap center, IWVWMap red, IWVWMap green, IWVWMap blue){
		checkNotNull(center);
		checkArgument(center.getType().isCenter());
		checkNotNull(red);
		checkArgument(red.getType().isRed());
		checkNotNull(green);
		checkArgument(green.getType().isGreen());
		checkNotNull(blue);		
		checkArgument(blue.getType().isBlue());
		this.maps = ImmutableMap.<IWVWMapType, IWVWMap>builder().put(CENTER_TYPE, center).put(RED_TYPE, red).put(GREEN_TYPE, green).put(BLUE_TYPE, blue).build();
	}
	

	public IWVWMap getCenterMap() {
		checkState(this.maps.containsKey(CENTER_TYPE));
		return this.maps.get(CENTER_TYPE);
	}

	public IWVWMap getRedMap() {
		checkState(this.maps.containsKey(RED_TYPE));
		return this.maps.get(RED_TYPE);
	}

	public IWVWMap getGreenMap() {
		checkState(this.maps.containsKey(GREEN_TYPE));
		return this.maps.get(GREEN_TYPE);
	}

	public IWVWMap getBlueMap() {
		checkState(this.maps.containsKey(BLUE_TYPE));
		return this.maps.get(BLUE_TYPE);
	}
	
	public String toString() {
		return Objects.toStringHelper(this).add("maps", this.maps).toString();
	}

}
