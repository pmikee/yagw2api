package api.model.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import api.model.IWVWArea;
import api.model.IWVWMap;
import api.model.IWVWMapType;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

class WVWArea extends AbstractHasChannel implements IWVWArea {
	private static final IWVWMapType CENTER_TYPE = WVWMapType.CENTER;
	private static final IWVWMapType RED_TYPE = WVWMapType.RED;
	private static final IWVWMapType GREEN_TYPE = WVWMapType.GREEN;
	private static final IWVWMapType BLUE_TYPE = WVWMapType.BLUE;
	
	private final Map<IWVWMapType, IWVWMap> maps;
	
	public WVWArea(IWVWMap center, IWVWMap red, IWVWMap green, IWVWMap blue){
		checkNotNull(center);
		checkArgument(center.getType().equals(CENTER_TYPE));
		checkNotNull(red);
		checkArgument(red.getType().equals(RED_TYPE));
		checkNotNull(green);
		checkArgument(green.getType().equals(GREEN_TYPE));
		checkNotNull(blue);		
		checkArgument(blue.getType().equals(BLUE_TYPE));
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
