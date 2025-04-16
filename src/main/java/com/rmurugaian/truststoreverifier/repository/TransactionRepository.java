package com.rmurugaian.truststoreverifier.repository;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.rmurugaian.truststoreverifier.model.TransactionDocument;
import java.util.UUID;

public interface TransactionRepository
    extends ReactiveCosmosRepository<TransactionDocument, UUID> {}
