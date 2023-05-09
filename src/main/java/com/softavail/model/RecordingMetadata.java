package com.softavail.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.micronaut.core.annotation.Introspected;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Introspected
public class RecordingMetadata {
    @NotBlank
    String filename;
    @NotBlank
    String callId;
    @NonNull
    Long from;
    @NonNull
    Long to;
    @NonNull
    LocalDateTime started;
    @NonNull
    Integer duration;
}
