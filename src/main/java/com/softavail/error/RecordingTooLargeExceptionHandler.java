package com.softavail.error;

import com.softavail.error.exceptions.RecordingTooLargeException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {RecordingTooLargeExceptionHandler.class, FileNotFoundExceptionHandler.class})
public class RecordingTooLargeExceptionHandler implements ExceptionHandler<RecordingTooLargeException, HttpResponse<?>>
{

  @Override
  public HttpResponse<?> handle(HttpRequest request, RecordingTooLargeException exception) {
    return HttpResponse.badRequest(exception.getMessage());
  }
}
