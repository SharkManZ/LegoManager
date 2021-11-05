package ru.shark.home.legomanager.configuration;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.shark.home.common.dao.util.ConverterUtil;

@Configuration
public class MapperConfig {

    @Bean
    public ConverterUtil getConverterUtil() {
        return new ConverterUtil();
    }

    @Bean
    public Mapper getMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        return mapper;
    }
}
