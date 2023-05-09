package com.softavail;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import reactor.core.publisher.Mono;

@Controller("/importer")
public class RecordingImporterController {
    @Post("/recording")
    public Mono<HttpStatus> sendRecording(){
        return null;
    }
}
