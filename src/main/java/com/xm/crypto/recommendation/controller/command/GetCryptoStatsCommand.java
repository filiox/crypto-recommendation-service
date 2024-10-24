package com.xm.crypto.recommendation.controller.command;

import com.xm.crypto.recommendation.dto.CryptoStatsDto;
import com.xm.crypto.recommendation.model.CryptoStats;
import com.xm.crypto.recommendation.model.Request;
import com.xm.crypto.recommendation.service.CryptoRecommendationService;
import com.xm.crypto.recommendation.validator.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCryptoStatsCommand implements Command<Request, CryptoStatsDto> {

    private static final Logger logger = LoggerFactory.getLogger(GetCryptoStatsCommand.class);

    private final CryptoRecommendationService cryptoRecommendationService;

    public GetCryptoStatsCommand(CryptoRecommendationService cryptoRecommendationService) {
        this.cryptoRecommendationService = cryptoRecommendationService;
    }

    @Override
    public CryptoStatsDto execute(Request request) {
        logger.info("Getting crypto statistics for symbol: {}", request.getSymbol());
        CryptoStatsDto result = toDto(DateUtils.getTimeRangeFromString(request.getStartDate(), request.getEndDate())
                .map(dateRange -> {
                    logger.debug("Date range provided: {} to {}", request.getStartDate(), request.getEndDate());
                    return cryptoRecommendationService.getCryptoStatsForSymbolAndDateRange(request.getSymbol(), dateRange);
                })
                .orElseGet(() -> {
                    logger.debug("No date range provided, getting stats for symbol only");
                    return cryptoRecommendationService.getCryptoStatsForSymbol(request.getSymbol());
                }));
        logger.info("Successfully retrieved crypto statistics for symbol: {}", request.getSymbol());
        return result;
    }

    private CryptoStatsDto toDto(CryptoStats cryptoStats){
        logger.debug("Converting CryptoStats to CryptoStatsDto for symbol");
        CryptoStatsDto cryptoStatsDto = new CryptoStatsDto();
        cryptoStatsDto
                .max(cryptoStats.getMaxPrice())
                .min(cryptoStats.getMinPrice())
                .oldest(cryptoStats.getOldestPrice())
                .newest(cryptoStats.getNewestPrice());
        return cryptoStatsDto;
    }
}