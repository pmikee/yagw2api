package de.justi.yagw2api.analyzer.impl;

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

import static com.google.common.base.Preconditions.checkArgument;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;

@javax.persistence.Converter
public final class LocalDateTimeConverter implements Converter, AttributeConverter<LocalDateTime, Date> {
	private static final long serialVersionUID = 5590460947565445621L;

	@Override
	public Object convertObjectValueToDataValue(final Object objectValue, final Session session) {
		if (objectValue == null) {
			return null;
		} else {
			checkArgument(objectValue instanceof LocalDateTime, "expected %s to be instance of %s", objectValue, LocalDateTime.class);
			return this.convertToDatabaseColumn((LocalDateTime) objectValue);
		}
	}

	@Override
	public Object convertDataValueToObjectValue(final Object dataValue, final Session session) {
		if (dataValue == null) {
			return null;
		} else {
			checkArgument(dataValue instanceof Date, "expected %s to be instance of %s", dataValue, Date.class);
			return this.convertToEntityAttribute((Date) dataValue);
		}
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public void initialize(final DatabaseMapping mapping, final Session session) {
	}

	@Override
	public Date convertToDatabaseColumn(final LocalDateTime attribute) {
		if (attribute == null) {
			return null;
		} else {
			final Instant instant = attribute.atZone(ZoneId.systemDefault()).toInstant();
			final Date res = Date.from(instant);
			return res;
		}
	}

	@Override
	public LocalDateTime convertToEntityAttribute(final Date dbData) {
		if (dbData == null) {
			return null;
		} else {
			final Instant instant = Instant.ofEpochMilli(dbData.getTime());
			final LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			return res;
		}
	}

}
