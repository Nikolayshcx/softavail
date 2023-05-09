package com.softavail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.softavail.model.RecordingMetadata;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Files;

@Singleton
public class RecordingImporterService {

  private final HttpClient   processingSystemClient;
  private final ObjectWriter mapper;
  private final String recordingsLocation;
  private final String       PROCESS_CALL = "/process";

  public RecordingImporterService(@Client("${processing.system.url}") HttpClient processingSystemClient,
                                  @Value("${recordings.location}") String recordingsLocation)
  {
    this.processingSystemClient = processingSystemClient;
    this.recordingsLocation     = recordingsLocation;
    this.mapper                 = new ObjectMapper().writer().withDefaultPrettyPrinter();
  }

  public Mono<HttpStatus> processRecording(RecordingMetadata metadata){

    String metadataJson;
    try {
      metadataJson = mapper.writeValueAsString(metadata);
    }
    catch (JsonProcessingException e) {
      return Mono.error(e);
    }

    MultipartBody request = MultipartBody.builder()
                           .addPart("metadata", metadataJson)
                           .addPart("mediaFile",
                                    metadata.getFilename(),
                                    new MediaType("video/ogg"),
                                    new File(recordingsLocation + metadata.getFilename()))
                           .build();

    return Mono.from(processingSystemClient.retrieve(HttpRequest.POST(PROCESS_CALL, request), HttpStatus.class));
  }
}
