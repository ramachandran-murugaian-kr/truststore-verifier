package com.rmurugaian.truststoreverifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;

public class CosmosKeyStoreUtils {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(CosmosKeyStoreUtils.class);

  // Follows the steps from https://learn.microsoft.com/en-us/azure/cosmos-db/emulator-linux
  public static void setupTrustStore(
      final String emulatorHost, final int emulatorPort, final String keyStorePassword)
      throws Exception {

    // Step 1: Create a temporary directory for the KeyStore
    final var tempFolder = Files.createTempDirectory("cosmos-emulator-keystore");
    final var certFile = tempFolder.resolve("cosmos-emulator.cert");
    final var keyStoreFile = tempFolder.resolve("azure-cosmos-emulator.keystore");

    // Step 2: Fetch and save the emulator certificate
    fetchCertificateWithOpenSSL(emulatorHost, emulatorPort, certFile.toString());

    // Step 3: Load the certificate into a KeyStore
    final var keyStore = createKeyStoreFromCertificate(certFile.toString(), keyStorePassword);

    // Step 4: Save the KeyStore to a file
    try (final var keyStoreOut = new FileOutputStream(keyStoreFile.toFile())) {
      keyStore.store(keyStoreOut, keyStorePassword.toCharArray());
    }

    // Step 5: Configure JVM to use the new KeyStore as the trust store
    System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
    System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);
    System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");

    log.info("TrustStore configured with certificate from: {}", certFile);
  }

  public static void setupTrustStore(final KeyStore keyStore, final String keyStorePassword)
      throws Exception {

    // Step 1: Create a temporary directory for the KeyStore
    final var tempFolder = Files.createTempDirectory("cosmos-emulator-keystore");
    final var keyStoreFile = tempFolder.resolve("azure-cosmos-emulator.keystore");

    // Step 4: Save the KeyStore to a file
    try (final var keyStoreOut = new FileOutputStream(keyStoreFile.toFile())) {
      keyStore.store(keyStoreOut, keyStorePassword.toCharArray());
    }

    // Step 5: Configure JVM to use the new KeyStore as the trust store
    System.setProperty("javax.net.ssl.trustStore", keyStoreFile.toString());
    System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
    System.setProperty("javax.net.ssl.trustStorePassword", keyStorePassword);

    log.info("TrustStore configured with certificate from emulator: {}", keyStoreFile);
  }

  public static void fetchCertificateWithOpenSSL(String host, int port, String outputPath)
      throws Exception {

    final var command =
        String.format(
            "openssl s_client -connect %s:%d </dev/null | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p'",
            host, port);

    // Execute the command using ProcessBuilder
    final var processBuilder = new ProcessBuilder("bash", "-c", command);
    processBuilder.redirectErrorStream(true); // Redirect error stream to standard output

    final var process = processBuilder.start();

    // Capture the output of the command
    String output;
    try (final var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      output = reader.lines().collect(Collectors.joining("\n"));
    }

    // Wait for the process to complete
    final var exitCode = process.waitFor();
    if (exitCode != 0) {
      throw new IllegalStateException("Command execution failed with exit code " + exitCode);
    }

    // Write the certificate to the output file
    try (final var fileWriter = new FileWriter(outputPath)) {
      fileWriter.write(output);
    }

    log.info("Certificate saved to {}", outputPath);
  }

  private static KeyStore createKeyStoreFromCertificate(String certPath, String keyStorePassword)
      throws Exception {
    // Load the PEM-formatted certificate
    final var cf = CertificateFactory.getInstance("X.509");
    try (final var fis = new FileInputStream(certPath)) {
      final var cert = (X509Certificate) cf.generateCertificate(fis);

      // Create a new KeyStore and add the certificate
      final var keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, keyStorePassword.toCharArray());
      keyStore.setCertificateEntry("cosmos-emulator", cert);

      return keyStore;
    }
  }
}
