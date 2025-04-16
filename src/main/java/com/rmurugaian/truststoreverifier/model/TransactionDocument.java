package com.rmurugaian.truststoreverifier.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

@Container(containerName = "TransactionDocument")
@Value
@Builder(toBuilder = true)
@Jacksonized
public class TransactionDocument {
  @Id @PartitionKey UUID id;
  String command;
  String value;
  String decrypted;
  String error;
}
