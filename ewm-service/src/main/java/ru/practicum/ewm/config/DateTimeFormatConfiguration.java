package ru.practicum.ewm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static ru.practicum.ewm.config.ContactAppConfig.DATE_FORMATTER;
import static ru.practicum.ewm.config.ContactAppConfig.DATE_TIME_FORMATTER;

@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.setDateFormatter(DATE_FORMATTER);
        registrar.setDateTimeFormatter(DATE_TIME_FORMATTER);
        registrar.registerFormatters(registry);
    }
}