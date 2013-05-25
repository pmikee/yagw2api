package model.wvw;

import model.IHasChannel;

public interface IWVWArea extends IHasChannel{
	IWVWMap getCenterMap();
	IWVWMap getRedMap();
	IWVWMap getGreenMap();
	IWVWMap getBlueMap();
}