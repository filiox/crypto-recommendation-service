package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.exception.CryptoDataMalformedException;
import com.xm.crypto.recommendation.exception.ValidationException;
import com.xm.crypto.recommendation.model.Crypto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CSVLoaderServiceTest {

    private CSVLoaderService csvLoaderService;

    private static final String FILE_NAME_PATTERN = "([A-Za-z]+)_values\\.csv$";

    @BeforeEach
    void setUp() {
        String csvDirectory = Paths.get("src", "test", "resources").toString();
        csvLoaderService = new CSVLoaderService(csvDirectory, FILE_NAME_PATTERN);
    }

    @Test
    @DisplayName("Load successfully all crypto data from CSV files.")
    void loadAllCryptos_withValidFiles_shouldLoadDataFromCSVFiles()  {
        Map<String, List<Crypto>> cryptoData = csvLoaderService.loadAllCryptos();

        assertThat(cryptoData).hasSize(5);

        // Verifying BTC data
        assertThat(cryptoData).containsKey("BTC");
        assertThat(cryptoData.get("BTC")).hasSize(100);

        // Verifying ETH data
        assertThat(cryptoData).containsKey("ETH");
        assertThat(cryptoData.get("ETH")).hasSize(95);

        // Verifying DOGE data
        assertThat(cryptoData).containsKey("DOGE");
        assertThat(cryptoData.get("DOGE")).hasSize(90);

        // Verifying LTC data
        assertThat(cryptoData).containsKey("LTC");
        assertThat(cryptoData.get("LTC")).hasSize(85);

        // Verifying XRP data
        assertThat(cryptoData).containsKey("XRP");
        assertThat(cryptoData.get("XRP")).hasSize(80);
    }

    @Test
    @DisplayName("An invalid name was provided for one of the input CSV files. A ValidationException should be thrown.")
    void loadAllCryptos_withInvalidFileName_shouldThrowValidationException() {
        String invalidPatternDirectory = Paths.get("src", "test", "resources", "invalid_filename").toString();
        csvLoaderService = new CSVLoaderService(invalidPatternDirectory, FILE_NAME_PATTERN);

        // This should throw ValidationException because of invalid file name format
        assertThatThrownBy(() -> csvLoaderService.loadAllCryptos())
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid file name format: BTC_values_1232.csv");
    }

    @Test
    @DisplayName("One of the CSVs contained malformed data in a record. A CryptoDataMalformedException should be thrown.")
    void loadAllCryptos_withMalformedCSV_shouldThrowCryptoDataMalformedException() {
        String malformedDataDirectory = Paths.get("src", "test", "resources", "malformed").toString();
        csvLoaderService = new CSVLoaderService(malformedDataDirectory, FILE_NAME_PATTERN);

        assertThatThrownBy(() -> csvLoaderService.loadAllCryptos())
                .isInstanceOf(CompletionException.class)
                .hasCauseInstanceOf(CryptoDataMalformedException.class)
                .hasMessageContaining("Error parsing line");
    }

    @Test
    @DisplayName("An invalid numeric value was displayed for one of the prices contained in the input CSVs. A CryptoDataMalformedException should be thrown")
    void loadAllCryptos_withInvalidNumberFormat_shouldThrowCryptoDataMalformedException() {
        String numberFormatExceptionDirectory = Paths.get("src", "test", "resources", "invalid_numbers").toString();
        csvLoaderService = new CSVLoaderService(numberFormatExceptionDirectory, FILE_NAME_PATTERN);

        assertThatThrownBy(() -> csvLoaderService.loadAllCryptos())
                .isInstanceOf(CompletionException.class)
                .hasCauseInstanceOf(CryptoDataMalformedException.class)
                .hasMessageContaining("Error parsing line");
    }
}