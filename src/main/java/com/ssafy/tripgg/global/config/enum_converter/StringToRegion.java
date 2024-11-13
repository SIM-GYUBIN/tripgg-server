package com.ssafy.tripgg.global.config.enum_converter;

import com.ssafy.tripgg.domain.course.entity.enums.Region;
import org.springframework.core.convert.converter.Converter;

//@Component
public class StringToRegion implements Converter<String, Region> {

    @Override
    public Region convert(String source) {
        return Region.valueOf(source.toUpperCase());
    }
}
