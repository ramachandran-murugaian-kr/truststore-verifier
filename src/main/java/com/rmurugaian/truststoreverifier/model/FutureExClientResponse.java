package com.rmurugaian.truststoreverifier.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class FutureExClientResponse {
  String decrypted;
  String error;
}
