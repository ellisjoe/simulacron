/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.simulacron.common.result;

import com.datastax.oss.protocol.internal.Message;
import com.datastax.oss.protocol.internal.response.error.ReadFailure;
import com.datastax.simulacron.common.codec.ConsistencyLevel;
import com.datastax.simulacron.common.codec.RequestFailureReason;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.InetAddress;
import java.util.Map;

import static com.datastax.oss.protocol.internal.ProtocolConstants.ErrorCode.READ_FAILURE;

public class ReadFailureResult extends RequestFailureResult {

  @JsonProperty("data_present")
  private final boolean dataPresent;

  public ReadFailureResult(
      ConsistencyLevel cl,
      int received,
      int blockFor,
      Map<InetAddress, RequestFailureReason> failureReasonByEndpoint,
      boolean dataPresent) {
    this(cl, received, blockFor, failureReasonByEndpoint, dataPresent, 0);
  }

  @JsonCreator
  public ReadFailureResult(
      @JsonProperty(value = "consistency", required = true) ConsistencyLevel cl,
      @JsonProperty(value = "received", required = true) int received,
      @JsonProperty(value = "block_for", required = true) int blockFor,
      @JsonProperty(value = "failure_reasons", required = true)
          Map<InetAddress, RequestFailureReason> failureReasonByEndpoint,
      @JsonProperty(value = "data_present", required = true) boolean dataPresent,
      @JsonProperty("delay_in_ms") long delayInMs) {
    super(READ_FAILURE, cl, received, blockFor, failureReasonByEndpoint, delayInMs);
    this.dataPresent = dataPresent;
  }

  @Override
  public Message toMessage() {
    return new ReadFailure(
        errorMessage,
        cl.getCode(),
        received,
        blockFor,
        failureReasonByEndpoint.size(),
        toIntMap(failureReasonByEndpoint),
        dataPresent);
  }
}
