package de.justi.yagw2api.analyzer.wvw.impl;

import com.google.inject.AbstractModule;

import de.justi.yagw2api.analyzer.IWVWAnalyzer;

public class AnalyzerWVWModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IWVWAnalyzer.class).to(WVWAnalyzer.class);
	}

}
