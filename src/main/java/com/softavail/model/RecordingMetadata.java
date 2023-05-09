package com.softavail.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class RecordingMetadata {
    String filename;
    String callId;
    Long from;
    Long to;
    LocalDateTime started;
    Integer duration;
}
