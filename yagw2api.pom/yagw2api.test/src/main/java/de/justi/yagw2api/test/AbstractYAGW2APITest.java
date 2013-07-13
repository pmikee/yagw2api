package de.justi.yagw2api.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class AbstractYAGW2APITest {
	private static final Logger LOGGER = Logger.getLogger(AbstractYAGW2APITest.class);

	@Rule
	public TestName testMethodName = new TestName();

	@Before
	public final void beforeAbstractYAGW2APITest() {
		LOGGER.info("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
		LOGGER.info(">>>> START TEST: " + this.testMethodName.getMethodName() + "\n");
	}

	@After
	public final void afterAbstractYAGW2APITest() {
		LOGGER.info("");
		LOGGER.info(">>>> DONE TEST:  " + this.testMethodName.getMethodName());
		LOGGER.info("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^" + "\n");
	}
}
