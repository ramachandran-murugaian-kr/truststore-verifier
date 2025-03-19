package com.rmurugaian.truststoreverifier.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Value
@Jacksonized
public class FutureExClientRequest {
  String command;
  String value;
}
