package com.softavail.config;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

@Singleton
public class LocalDateTimeConverter implements TypeConverter<String, LocalDateTime>
{

  @Override
  public Optional<LocalDateTime> convert(String inputDateTime, Class<LocalDateTime> targetType, ConversionContext context) {
    if (inputDateTime != null) {
      try {
        var offsetDateTime =
            LocalDateTime.from(new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").toFormatter(Locale.ENGLISH).parse(inputDateTime));
        return Optional.of(offsetDateTime);
      } catch (DateTimeParseException e) {
        context.reject(inputDateTime, e);
      }
    }

    return Optional.empty();
  }
}
