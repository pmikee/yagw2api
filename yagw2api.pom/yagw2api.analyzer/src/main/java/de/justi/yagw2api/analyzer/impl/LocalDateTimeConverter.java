package de.justi.yagw2api.analyzer.impl;

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
	public Object convertObjectValueToDataValue(Object objectValue, Session session) {
		if (objectValue == null) {
			return null;
		} else {
			checkArgument(objectValue instanceof LocalDateTime, "expected %s to be instance of %s", objectValue, LocalDateTime.class);
			return convertToDatabaseColumn((LocalDateTime) objectValue);
		}
	}

	@Override
	public Object convertDataValueToObjectValue(Object dataValue, Session session) {
		if (dataValue == null) {
			return null;
		} else {
			checkArgument(dataValue instanceof Date, "expected %s to be instance of %s", dataValue, Date.class);
			return convertToEntityAttribute((Date) dataValue);
		}
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public void initialize(DatabaseMapping mapping, Session session) {
	}

	@Override
	public Date convertToDatabaseColumn(LocalDateTime attribute) {
		if (attribute == null) {
			return null;
		} else {
			final Instant instant = attribute.atZone(ZoneId.systemDefault()).toInstant();
			final Date res = Date.from(instant);
			return res;
		}
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		if(dbData == null){
			return null;
		}else{
			final Instant instant = Instant.ofEpochMilli(dbData.getTime());
			final LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			return res;
		}
	}

}
