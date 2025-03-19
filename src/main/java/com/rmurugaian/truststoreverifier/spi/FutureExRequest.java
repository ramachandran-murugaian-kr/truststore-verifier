package com.rmurugaian.truststoreverifier.spi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@Value
public class FutureExRequest {

  @JsonProperty("AO")
  String ao;

  @JsonProperty("TI")
  String ti;

  @JsonProperty("AK")
  String ak;

  @JsonProperty("GM")
  String gm;

  @JsonProperty("AI")
  String ai;

  @JsonProperty("RO")
  String ro;

  @JsonProperty("TW")
  String tw;

  @JsonProperty("FS")
  String fs;

  @JsonProperty("VA")
  String va;

  @JsonProperty("TD")
  String td;

  @JsonProperty("WT")
  String wt;

  @JsonProperty("JW")
  String jw;
}
