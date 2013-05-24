package model;

public interface IWVWArea extends IHasChannel{
	IWVWMap getCenterMap();
	IWVWMap getRedMap();
	IWVWMap getGreenMap();
	IWVWMap getBlueMap();
}