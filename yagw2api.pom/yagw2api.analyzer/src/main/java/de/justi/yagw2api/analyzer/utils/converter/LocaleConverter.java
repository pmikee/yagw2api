package de.justi.yagw2api.analyzer.utils.converter;

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
