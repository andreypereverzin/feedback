package eu.ec.feedback.config;

import eu.ec.feedback.model.ContactType;
import org.springframework.core.convert.converter.Converter;

public class StringToContactTypeConverter implements Converter<String, ContactType> {
    @Override
    public ContactType convert(String source) {
        return ContactType.valueOf(source.toUpperCase());
    }
}
