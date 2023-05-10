package com.softavail.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Introspected
public class RecordingMetadata {
    @NotBlank
    String  filename;
    @NotBlank
    @Size(max = 18, min = 18)
    String  callId;
    @NonNull
    Long from;
    @NonNull
    Long    to;
    @NonNull
    Date    started;
    @NonNull
    Integer duration;
}
