package de.justi.yagw2api.analyzer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;

public abstract class AbstractAnalyzerTest {
	@BeforeClass
	public static void beforeClass() {
		YAGW2APIAnalyzer.changeYAGW2APIAnalyzerInstance(YAGW2APIAnalyzer.TEST);
	}
	@Before
	public void before() {
		// nothing to do
	}	
	
	@After
	public void after() {
		YAGW2APIAnalyzerPersistence.getDefaultEMF().close();
	}
	
	@AfterClass
	public static void afterClass() {
		// nothing to do
	}
}
