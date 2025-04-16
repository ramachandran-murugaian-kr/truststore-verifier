package com.rmurugaian.truststoreverifier.spi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Value
@Jacksonized
public class FutureExResponse {
  @JsonProperty("COMMAND")
  String command;

  @JsonProperty("AM")
  String am;

  @JsonProperty("AN")
  String an;

  @JsonProperty("AO")
  String ao;

  @JsonProperty("BB")
  String bb;

  @JsonProperty("ER")
  String er;
}
