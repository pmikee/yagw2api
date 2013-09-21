package de.justi.yagw2api.analyzer.utils;

/*
 * <~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * YAGW2API-Analyzer
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


import static com.google.common.base.Preconditions.checkArgument;

import java.util.Locale;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

public final class LocaleConverter implements Converter {
	private static final long serialVersionUID = 6076012675486780488L;

	@Override
	public Object convertObjectValueToDataValue(Object objectValue, Session session) {
		if (objectValue != null) {
			checkArgument(objectValue instanceof Locale);
			return ((Locale) objectValue).toLanguageTag();
		} else {
			return null;
		}
	}

	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue != null) {
			checkArgument(dataValue instanceof String);
			return Locale.forLanguageTag((String) dataValue);
		} else {
			return null;
		}

	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public void initialize(DatabaseMapping mapping, Session session) {
		// nothing to do
	}

}
