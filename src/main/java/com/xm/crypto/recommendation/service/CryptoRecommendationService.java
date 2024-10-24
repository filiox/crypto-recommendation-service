package com.xm.crypto.recommendation.service;

import com.xm.crypto.recommendation.exception.ResourceNotFoundException;
import com.xm.crypto.recommendation.model.Crypto;
import com.xm.crypto.recommendation.model.CryptoNormalizedRange;
import com.xm.crypto.recommendation.model.CryptoStats;
import com.xm.crypto.recommendation.model.DateRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CryptoRecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(CryptoRecommendationService.class);

    private final Map<String, List<Crypto>> cryptoData;

    public CryptoRecommendationService(CSVLoaderService csvLoaderService) {
        logger.info("Initializing CryptoRecommendationService...");
        this.cryptoData = csvLoaderService.loadAllCryptos();
        logger.info("CryptoRecommendationService initialized successfully.");
    }

    private Optional<List<Crypto>> getCryptoInfo(String symbol) {
        logger.debug("Fetching crypto info for symbol: {}", symbol);
        return Optional.ofNullable(cryptoData.get(symbol));
    }

    public BigDecimal getNormalizedRangeForSymbol(String symbol) {
        logger.info("Getting normalized range for specified symbol: {}", symbol);
        List<Crypto> cryptoInfo = getCryptoInfo(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("No data found for symbol: " + symbol));

        BigDecimal minPrice = cryptoInfo.stream()
                .min(Comparator.comparing(Crypto::getPrice))
                .orElseThrow(() -> new ResourceNotFoundException("No minimum price found"))
                .getPrice();

        BigDecimal maxPrice = cryptoInfo.stream()
                .max(Comparator.comparing(Crypto::getPrice))
                .orElseThrow(() -> new ResourceNotFoundException("No maximum price found"))
                .getPrice();

        BigDecimal normalizedRange = (maxPrice.subtract(minPrice)).divide(minPrice, RoundingMode.HALF_UP);
        logger.info("Normalized range for symbol {}: {}", symbol, normalizedRange);
        return normalizedRange;
    }

    public CryptoStats getCryptoStatsForSymbolAndDateRange(String symbol, DateRange dateRange) {
        logger.info("Getting crypto statistics for symbol {} and date range: {}", symbol, dateRange);
        List<Crypto> cryptoInfo = getCryptoInfo(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("No data found for symbol: " + symbol));

        List<Crypto> filteredCryptoInfo = cryptoInfo.stream()
                .filter(crypto -> !crypto.getTimestamp().toLocalDate().isBefore(dateRange.getStart())
                        && !crypto.getTimestamp().toLocalDate().isAfter(dateRange.getEnd()))
                .toList();

        if (filteredCryptoInfo.isEmpty()) {
            logger.warn("No data found for symbol: {} in the specified time range", symbol);
            throw new ResourceNotFoundException("No data found for symbol: " + symbol + " in the specified time range");
        }

        return getCryptoStats(filteredCryptoInfo);
    }

    public CryptoStats getCryptoStatsForSymbol(String symbol) {
        logger.info("Getting crypto statistics for symbol: {}", symbol);
        List<Crypto> cryptoInfo = getCryptoInfo(symbol)
                .orElseThrow(() -> new ResourceNotFoundException("No data found for symbol: " + symbol));

        return getCryptoStats(cryptoInfo);
    }

    public List<CryptoNormalizedRange> getSortedByNormalizedRangedForAllCryptos() {
        logger.info("Getting normalized range sorted in descending order...");
        return cryptoData.keySet().stream()
                .map(symbol -> new CryptoNormalizedRange(symbol, getNormalizedRangeForSymbol(symbol)))
                .sorted(Comparator.comparing(CryptoNormalizedRange::getNormalizedRange).reversed())
                .collect(Collectors.toList());
    }

    public CryptoNormalizedRange getCryptoHighestNormalizedRangeForDate(LocalDate targetDate) {
        logger.info("Retrieving highest normalized range for date: {}", targetDate);
        return getAllCryptoInfoForDate(targetDate).entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(entry -> new CryptoNormalizedRange(entry.getKey(), getNormalizedRangeForSymbol(entry.getKey())))
                .max(Comparator.comparing(CryptoNormalizedRange::getNormalizedRange))
                .orElseThrow(() -> new ResourceNotFoundException("No cryptocurrencies found with valid data for the date: " + targetDate));
    }

    private Map<String, List<Crypto>> getAllCryptoInfoForDate(LocalDate targetDate) {
        logger.debug("Retrieving all crypto info for date: {}", targetDate);
        return cryptoData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> filterCryptosByDate(entry.getValue(), targetDate)
                ));
    }

    private List<Crypto> filterCryptosByDate(List<Crypto> cryptos, LocalDate targetDate) {
        logger.debug("Filtering cryptos by date: {}", targetDate);
        return cryptos.stream()
                .filter(c -> c.getTimestamp().toLocalDate().equals(targetDate))
                .collect(Collectors.toList());
    }

    private CryptoStats getCryptoStats(List<Crypto> cryptoInfo) {
        logger.debug("Calculating crypto stats...");
        BigDecimal oldestPrice = cryptoInfo.stream()
                .min(Comparator.comparing(Crypto::getTimestamp))
                .orElseThrow()
                .getPrice();

        BigDecimal newestPrice = cryptoInfo.stream()
                .max(Comparator.comparing(Crypto::getTimestamp))
                .orElseThrow()
                .getPrice();

        BigDecimal minPrice = cryptoInfo.stream()
                .min(Comparator.comparing(Crypto::getPrice))
                .orElseThrow(() -> new ResourceNotFoundException("No minimum price found"))
                .getPrice();

        BigDecimal maxPrice = cryptoInfo.stream()
                .max(Comparator.comparing(Crypto::getPrice))
                .orElseThrow(() -> new ResourceNotFoundException("No maximum price found"))
                .getPrice();

        return new CryptoStats(oldestPrice, newestPrice, minPrice, maxPrice);
    }
}