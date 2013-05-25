package model.wvw;

public interface IWVWMapBuilder {
	IWVWMap build();
	IWVWMapBuilder type(IWVWMapType type);
	IWVWMapBuilder objective(IWVWObjective objective);
}
