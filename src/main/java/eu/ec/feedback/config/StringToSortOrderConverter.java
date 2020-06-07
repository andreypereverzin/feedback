package eu.ec.feedback.config;

import eu.ec.feedback.service.SortOrder;
import org.springframework.core.convert.converter.Converter;

public class StringToSortOrderConverter implements Converter<String, SortOrder> {
    @Override
    public SortOrder convert(String source) {
        return SortOrder.valueOf(source.toUpperCase());
    }
}
