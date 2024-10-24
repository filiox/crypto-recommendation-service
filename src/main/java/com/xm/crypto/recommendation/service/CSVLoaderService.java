package com.xm.crypto.recommendation.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.xm.crypto.recommendation.exception.CryptoDataMalformedException;
import com.xm.crypto.recommendation.exception.ValidationException;
import com.xm.crypto.recommendation.model.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CSVLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(CSVLoaderService.class);
    private final String csvDirectory;
    private final Pattern fileNamePattern;

    public CSVLoaderService(String csvDirectory, String fileNamePattern) {
        this.csvDirectory = csvDirectory;
        this.fileNamePattern = Pattern.compile(fileNamePattern);
    }

    public Map<String, List<Crypto>> loadAllCryptos() {
        logger.info("Loading all cryptos from directory: {}", csvDirectory);
        try (var directoryStream = Files.newDirectoryStream(Paths.get(csvDirectory), "*.csv")) {
            List<CompletableFuture<Map.Entry<String, List<Crypto>>>> futures = streamOf(directoryStream)
                    .map(this::processCsvFileAsync)
                    .toList();

            List<Map.Entry<String, List<Crypto>>> results = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            logger.info("Successfully loaded all cryptos from directory: {}", csvDirectory);
            return results.stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            logger.error("Error reading files from directory: {}", csvDirectory, e);
            throw new CryptoDataMalformedException("Error reading files from directory: " + csvDirectory, e);
        }
    }

    private <T> Stream<T> streamOf(DirectoryStream<T> directoryStream) {
        return StreamSupport.stream(directoryStream.spliterator(), false);
    }

    private CompletableFuture<Map.Entry<String, List<Crypto>>> processCsvFileAsync(Path filePath) {
        String symbol = extractSymbolFromFileName(filePath);
        logger.info("Processing CSV file asynchronously: {}", filePath);
        return CompletableFuture.supplyAsync(() -> {
            try {
                return loadCryptoData(filePath)
                        .map(cryptos -> Map.entry(symbol, cryptos))
                        .orElseThrow(() -> new CryptoDataMalformedException("Failed to load data from file: " + filePath));
            } catch (Exception e) {
                logger.error("Error processing CSV file: {}", filePath, e);
                throw e;
            }
        });
    }

    private Optional<List<Crypto>> loadCryptoData(Path filePath) {
        logger.info("Loading crypto data from file: {}", filePath);
        try (var csvReader = new CSVReader(new FileReader(filePath.toFile()))) {
            List<String[]> records = csvReader.readAll();
            List<Crypto> cryptos = records.stream()
                    .skip(1)
                    .map(this::parseCryptoLine)
                    .toList();
            logger.info("Successfully loaded crypto data from file: {}", filePath);
            return Optional.of(cryptos);
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            throw new CryptoDataMalformedException("Error reading CSV file: " + filePath, e);
        }
    }

    private Crypto parseCryptoLine(String[] data) {
        if (data.length != 3) {
            logger.error("Malformed CSV line: {}", String.join(",", data));
            throw new CryptoDataMalformedException("Malformed CSV line: " + String.join(",", data));
        }
        try {
            long timestamp = Long.parseLong(data[0]);
            BigDecimal price = new BigDecimal(data[2]);
            return new Crypto(data[1], LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC), price);
        } catch (NumberFormatException e) {
            logger.error("Error parsing line: {}", String.join(",", data), e);
            throw new CryptoDataMalformedException("Error parsing line: " + String.join(",", data), e);
        }
    }

    private String extractSymbolFromFileName(Path filePath) {
        Matcher matcher = fileNamePattern.matcher(filePath.getFileName().toString());
        if (!matcher.matches()) {
            logger.error("Invalid file name format: {}", filePath.getFileName().toString());
            throw new ValidationException("Invalid file name format: " + filePath.getFileName().toString());
        }
        return matcher.group(1);
    }
}