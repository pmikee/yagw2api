package de.justi.yagw2api.test;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Test
 * _____________________________________________________________
 * Copyright (C) 2012 - 2013 Julian Stitz
 * _____________________________________________________________
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>
 */


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractYAGW2APITest {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractYAGW2APITest.class);

	@Rule
	public final TestName testMethodName = new TestName();

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
