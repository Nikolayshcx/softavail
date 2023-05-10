package com.softavail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.softavail.model.RecordingMetadata;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.multipart.MultipartBody;
import jakarta.inject.Singleton;
import org.javaync.io.AsyncFiles;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;

@Singleton
public class RecordingImporterService {

  private final HttpClient   processingSystemClient;
  private final ObjectWriter mapper;
  private final String recordingsLocation;
  public static final String       PROCESS_CALL = "/process";

  public RecordingImporterService(@Client("${processing.system.url}") HttpClient processingSystemClient,
                                  @Value("${recordings.location}") String recordingsLocation)
  {
    this.processingSystemClient = processingSystemClient;
    this.recordingsLocation     = recordingsLocation;
    this.mapper                 = new ObjectMapper().writer().withDefaultPrettyPrinter();
  }

  /**
   * Assemble a multipart/form-data request and send it to the processing system
   * @param metadata the metadata of the recorded call
   * @return the response of the processing system,
   * including httpStatus and location header of the processed resource
   */
  public Mono<HttpResponse<?>> processRecording(RecordingMetadata metadata)
  {

    String metadataJson;
    try {
      metadataJson = mapper.writeValueAsString(metadata);
    }
    catch (JsonProcessingException e) {
      return Mono.error(e);
    }

    return Mono.fromFuture(AsyncFiles.readAllBytes(recordingsLocation + File.separator + metadata.getFilename()))
               .publishOn(Schedulers.boundedElastic())
               .map(fileBytes -> MultipartBody.builder()
                                              .addPart("metadata", metadataJson)
                                              .addPart("mediaFile", metadata.getFilename(),
                                                       new MediaType("video/ogg"), fileBytes)
                                              .build())
               .flatMap(request -> Mono.from(processingSystemClient.exchange(HttpRequest.POST(PROCESS_CALL, request)
                                                                                        .contentType(MediaType.MULTIPART_FORM_DATA_TYPE),
                                                                             Void.class)));
  }
}
