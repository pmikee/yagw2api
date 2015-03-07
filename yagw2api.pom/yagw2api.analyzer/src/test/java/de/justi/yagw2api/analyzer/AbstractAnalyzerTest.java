package de.justi.yagw2api.analyzer;

/*
 * @formatter:off<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
 * _____________________________________________________________
 * Copyright (C) 2012 - 2015 Julian Stitz
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
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>@formatter:on
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

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
