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
  private String command;

  @JsonProperty("AM")
  private String am;

  @JsonProperty("AN")
  private String an;

  @JsonProperty("AO")
  private String ao;

  @JsonProperty("BB")
  private String bb;

  @JsonProperty("ER")
  private String er;
}
