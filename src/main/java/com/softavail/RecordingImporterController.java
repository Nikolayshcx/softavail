package com.softavail;

import com.softavail.model.RecordingMetadata;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Controller("/importer")
public class RecordingImporterController {
    private final RecordingImporterService recordingImporterService;

    public RecordingImporterController(RecordingImporterService recordingImporterService
                                      ) {this.recordingImporterService = recordingImporterService;}

    @Post("/recording")
    @Status(HttpStatus.ACCEPTED)
    public Mono<HttpResponse<?>> sendRecording(@Body @Valid RecordingMetadata metadata){
        return recordingImporterService.processRecording(metadata);
    }
}
