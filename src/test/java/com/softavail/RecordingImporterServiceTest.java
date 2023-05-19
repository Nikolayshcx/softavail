package com.softavail;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.softavail.model.RecordingMetadata;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
@SuppressWarnings("ResultOfMethodCallIgnored")
class RecordingImporterServiceTest
{

  WireMockServer wireMockServer;
  RecordingImporterService recImporterService;
  File recording;
  String PROC_SYSTEM_HOST = "http://localhost";
  int PROC_SYSTEM_PORT = 8084;
  String testRecordingsLocation = System.getProperty("user.dir") + File.separator + "testRecordings";
  String uploadedLocation = "test/created/resource/location";

  @BeforeEach
  void setUp() throws MalformedURLException
  {
    wireMockServer = new WireMockServer(PROC_SYSTEM_PORT);
    wireMockServer.start();
    WireMock.configureFor(PROC_SYSTEM_HOST, PROC_SYSTEM_PORT);
    wireMockServer.stubFor(post(urlEqualTo(RecordingImporterService.PROCESS_CALL))
                               .withMultipartRequestBody(aMultipart())
                               .willReturn(aResponse().withStatus(202)
                                                      .withHeader("Location",
                                                                  uploadedLocation)));

    String filename = "testRecording.wav";
    log.info("Generated filename - " + filename);
    createTestRecording(filename);

    recImporterService = new RecordingImporterService(HttpClient.create(new URL(PROC_SYSTEM_HOST + ":" + PROC_SYSTEM_PORT)),
                                                      testRecordingsLocation,
                                                      1000);
  }

  @Test
  void processRecording()
  {
    StepVerifier.create(recImporterService.processRecording(new RecordingMetadata().setFilename(recording.getName())
                                                                                   .setCallId("test_call_id")
                                                                                   .setFrom(111111111111L)
                                                                                   .setTo(222222222222L)
                                                                                   .setStarted(Date.from(Instant.now()))
                                                                                   .setDuration(120)))
        .expectNextMatches(s -> {
          assertNotNull(s.getStatus());
          assertEquals(HttpStatus.ACCEPTED, s.getStatus());
          assertNotNull(s.getHeaders().get("Location"));
          assertEquals(uploadedLocation, s.getHeaders().get("Location"));
          log.info("Received response - status [" + s.getStatus() + "]"
                   + ", headers [" + s.getHeaders().asMap() + "]"
                   + ", body [" + (s.getBody().isPresent() ? s.getBody().get() : "null") + "]");
          return true;
        })
        .expectComplete()
        .verify();
  }

  @AfterEach
  void tearDown()
  {
    recording.delete();
    recording.getParentFile().delete();
  }

  private void createTestRecording(String filename)
  {
    recording = new File(testRecordingsLocation, filename);
    try {
      if(! recording.getParentFile().exists()) recording.getParentFile().mkdirs();

      while (!recording.createNewFile() && !recording.exists()){
        log.info( "File with name - [" + filename + "], already exists. Generating new test filename.");

        recording.delete();
        filename  = RandomStringUtils.random(16) + ".wav";
        recording = new File(filename);
      }
    }
    catch (IOException e) {
      log.error("Couldn't create test recording!");
      throw new RuntimeException(e);
    }
  }
}
