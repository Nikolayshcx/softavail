package com.softavail.error.exceptions;

public class RecordingTooLargeException extends RuntimeException
{
  public RecordingTooLargeException()
  {
    super("Recording file is too large for streaming!");
  }

  public RecordingTooLargeException(String message)
  {
    super(message);
  }

  public RecordingTooLargeException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public RecordingTooLargeException(Throwable cause)
  {
    super(cause);
  }

  protected RecordingTooLargeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
