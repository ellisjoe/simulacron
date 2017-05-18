package com.datastax.simulacron.common.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.datastax.oss.protocol.internal.ProtocolConstants.ErrorCode.UNPREPARED;

public class UnpreparedResult extends ErrorResult {

  public UnpreparedResult(String errorMessage) {
    this(errorMessage, 0);
  }

  @JsonCreator
  public UnpreparedResult(
      @JsonProperty(value = "message", required = true) String errorMessage,
      @JsonProperty("delay_in_ms") long delayInMs) {
    super(UNPREPARED, errorMessage, delayInMs);
  }
}
