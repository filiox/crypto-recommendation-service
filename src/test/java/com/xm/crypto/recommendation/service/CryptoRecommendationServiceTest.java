package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.exception.ResourceNotFoundException;
import com.xm.crypto.recommendation.model.Crypto;
import com.xm.crypto.recommendation.model.CryptoNormalizedRange;
import com.xm.crypto.recommendation.model.CryptoStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoRecommendationServiceTest {

    @Mock
    private CSVLoaderService csvLoaderService;

    private CryptoRecommendationService cryptoRecommendationService;

    @BeforeEach
    void setUp() {
        // Mock data for BTC
        List<Crypto> btcData = Arrays.asList(
                new Crypto("BTC", LocalDateTime.ofEpochSecond(1641013200, 0, ZoneOffset.UTC), new BigDecimal("36823.19")),
                new Crypto("BTC", LocalDateTime.ofEpochSecond(1641016200, 0, ZoneOffset.UTC), new BigDecimal("38161.69")),
                new Crypto("BTC", LocalDateTime.ofEpochSecond(1641017200, 0, ZoneOffset.UTC), new BigDecimal("35201.17"))
        );

        // Mock data for ETH
        List<Crypto> ethData = Arrays.asList(
                new Crypto("ETH", LocalDateTime.ofEpochSecond(1641013200, 0, ZoneOffset.UTC), new BigDecimal("39856.49")),
                new Crypto("ETH", LocalDateTime.ofEpochSecond(1641016200, 0, ZoneOffset.UTC), new BigDecimal("45636.34")),
                new Crypto("ETH", LocalDateTime.ofEpochSecond(1641017200, 0, ZoneOffset.UTC), new BigDecimal("34857.67"))
        );

        when(csvLoaderService.loadAllCryptos()).thenReturn(Map.of("BTC", btcData, "ETH", ethData));
        cryptoRecommendationService = new CryptoRecommendationService(csvLoaderService);
    }

    @Test
    void getNormalizedRangeForSymbol_shouldReturnCorrectRange() {
        BigDecimal normalizedRange = cryptoRecommendationService.getNormalizedRangeForSymbol("BTC");

        assertThat(normalizedRange)
                .isNotNull()
                .isEqualByComparingTo(new BigDecimal("0.08"));
    }

    @Test
    void getNormalizedRangeForSymbol_shouldThrowExceptionWhenSymbolNotFound() {
        assertThatThrownBy(() -> cryptoRecommendationService.getNormalizedRangeForSymbol("DOGE"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No data found for symbol: DOGE");
    }

    @Test
    void getCryptoStatsForSymbol_shouldReturnCorrectStats() {
        CryptoStats stats = cryptoRecommendationService.getCryptoStatsForSymbol("BTC");

        assertThat(stats).isNotNull();
        assertThat(stats.getOldestPrice()).isEqualByComparingTo(new BigDecimal("36823.19"));
        assertThat(stats.getNewestPrice()).isEqualByComparingTo(new BigDecimal("35201.17"));
        assertThat(stats.getMinPrice()).isEqualByComparingTo(new BigDecimal("35201.17"));
        assertThat(stats.getMaxPrice()).isEqualByComparingTo(new BigDecimal("38161.69"));
    }

    @Test
    void getSortedByNormalizedRangedForAllCryptos_shouldReturnSortedCryptos() {
        List<CryptoNormalizedRange> sortedRanges = cryptoRecommendationService.getSortedByNormalizedRange();

        assertThat(sortedRanges)
                .isNotEmpty()
                .hasSize(2)
                .extracting(CryptoNormalizedRange::getSymbol, CryptoNormalizedRange::getNormalizedRange)
                .containsExactly(
                        tuple("ETH", new BigDecimal("0.31")),
                        tuple("BTC", new BigDecimal("0.08"))
                );
    }

    @Test
    void getCryptoHighestNormalizedRangeForDate_shouldReturnHighestNormalizedRange() {
        CryptoNormalizedRange highestNormalizedRange = cryptoRecommendationService.getHighestNormalizedRangeForDate(LocalDate.of(2022, 1, 1));

        assertThat(highestNormalizedRange).isNotNull();
        assertThat(highestNormalizedRange.getSymbol()).isEqualTo("ETH");
        assertThat(highestNormalizedRange.getNormalizedRange()).isEqualByComparingTo(new BigDecimal("0.31"));
    }

    @Test
    void getCryptoHighestNormalizedRangeForDate_shouldThrowExceptionWhenNoDataForDate() {
        assertThatThrownBy(() -> cryptoRecommendationService.getHighestNormalizedRangeForDate(LocalDate.of(2025, 1, 1)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No cryptocurrencies found with valid data for the date");
    }
}
