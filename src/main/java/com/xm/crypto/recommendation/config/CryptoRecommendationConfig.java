package com.xm.crypto.recommendation.config;

import com.xm.crypto.recommendation.controller.command.GetCryptoSortedByNormalizedRangeCommand;
import com.xm.crypto.recommendation.controller.command.GetCryptoStatsCommand;
import com.xm.crypto.recommendation.controller.command.GetHighestNormalizedRangeCommand;
import com.xm.crypto.recommendation.controller.delegate.CryptoRecommendationDelegate;
import com.xm.crypto.recommendation.service.CSVLoaderService;
import com.xm.crypto.recommendation.service.CryptoRecommendationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoRecommendationConfig {

    @Bean
    public CSVLoaderService csvLoaderService(@Value("${csv.dir}") String csvDirectory,
                                             @Value("${csv.file-name-pattern}") String fileNamePattern) {
        return new CSVLoaderService(csvDirectory, fileNamePattern);
    }

    @Bean
    public CryptoRecommendationService cryptoRecommendationService(CSVLoaderService csvLoaderService) {
        return new CryptoRecommendationService(csvLoaderService);
    }

    @Bean
    public GetCryptoSortedByNormalizedRangeCommand getCryptoSortedByNormalizedRangeCommand(CryptoRecommendationService cryptoRecommendationService) {
        return new GetCryptoSortedByNormalizedRangeCommand(cryptoRecommendationService);
    }

    @Bean
    public GetCryptoStatsCommand getCryptoStatsCommand(CryptoRecommendationService cryptoRecommendationService) {
        return new GetCryptoStatsCommand(cryptoRecommendationService);
    }

    @Bean
    public GetHighestNormalizedRangeCommand getHighestNormalizedRangeCommand(CryptoRecommendationService cryptoRecommendationService) {
        return new GetHighestNormalizedRangeCommand(cryptoRecommendationService);
    }

    @Bean
    public CryptoRecommendationDelegate cryptoRecommendationDelegate(GetCryptoSortedByNormalizedRangeCommand getCryptoSortedByNormalizedRangeCommand,
                                                                     GetCryptoStatsCommand getCryptoStatsCommand,
                                                                     GetHighestNormalizedRangeCommand getHighestNormalizedRangeCommand) {
        return new CryptoRecommendationDelegate(getHighestNormalizedRangeCommand, getCryptoStatsCommand, getCryptoSortedByNormalizedRangeCommand);
    }
}