package de.justi.gw2.model.wvw;

public interface IWVWMapType{
	String getLabel();
	boolean isCenter();
	boolean isRed();
	boolean isGreen();
	boolean isBlue();
	
	IWVWMapType getImmutableReference();
}
