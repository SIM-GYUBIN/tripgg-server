package com.ssafy.tripgg.global.config.enum_converter;

import com.ssafy.tripgg.domain.course.entity.enums.OrderType;
import org.springframework.core.convert.converter.Converter;

//@Component
public class StringToOrderBy implements Converter<String, OrderType> {

    @Override
    public OrderType convert(String source) {
        return OrderType.valueOf(source.toUpperCase());
    }
}
