package de.justi.yagw2api.analyzer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import de.justi.yagw2api.analyzer.entities.YAGW2APIAnalyzerPersistence;
import de.justi.yagw2api.test.AbstractYAGW2APITest;

public abstract class AbstractAnalyzerTest extends AbstractYAGW2APITest {
	@BeforeClass
	public final static void beforeClassAbstractAnalyzerTest() {
		YAGW2APIAnalyzer.changeYAGW2APIAnalyzerInstance(YAGW2APIAnalyzer.TEST);
	}

	@Before
	public final void beforeAbstractAnalyzerTest() {
		// nothing to do
	}

	@After
	public final void afterAbstractAnalyzerTest() {
		YAGW2APIAnalyzerPersistence.getDefaultEMF().close();
	}

	@AfterClass
	public final static void afterClassAbstractAnalyzerTest() {
		// nothing to do
	}
}
