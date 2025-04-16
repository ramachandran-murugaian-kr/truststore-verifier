package com.rmurugaian.truststoreverifier.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class FutureExClientResponse {
  UUID id;
  String decrypted;
  String error;
}
