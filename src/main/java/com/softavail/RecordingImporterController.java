package com.softavail;

import com.softavail.model.RecordingMetadata;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;

import javax.validation.Valid;
import java.io.IOException;

@Controller("/importer")
public class RecordingImporterController {
    private final RecordingImporterService recordingImporterService;

    public RecordingImporterController(RecordingImporterService recordingImporterService
                                      ) {this.recordingImporterService = recordingImporterService;}

    @Post("/recording")
    public Publisher<HttpResponse<Void>> sendRecording(@Body @Valid RecordingMetadata metadata) throws IOException
    {
        return recordingImporterService.processRecording(metadata);
    }
}
