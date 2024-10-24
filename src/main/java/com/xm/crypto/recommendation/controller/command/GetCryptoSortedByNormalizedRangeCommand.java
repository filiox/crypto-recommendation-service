package com.xm.crypto.recommendation.controller.command;

import com.xm.crypto.recommendation.dto.CryptoNormalizedRangeDto;
import com.xm.crypto.recommendation.model.CryptoNormalizedRange;
import com.xm.crypto.recommendation.model.Request;
import com.xm.crypto.recommendation.service.CryptoRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class GetCryptoSortedByNormalizedRangeCommand implements Command<Request, List<CryptoNormalizedRangeDto>> {

    private static final Logger logger = LoggerFactory.getLogger(GetCryptoSortedByNormalizedRangeCommand.class);
    private final CryptoRecommendationService cryptoRecommendationService;

    public GetCryptoSortedByNormalizedRangeCommand(CryptoRecommendationService cryptoRecommendationService) {
        this.cryptoRecommendationService = cryptoRecommendationService;
    }

    @Override
    public List<CryptoNormalizedRangeDto> execute(Request requestDto) {
        logger.info("Getting normalized range for each crypto in descending order.");
        List<CryptoNormalizedRangeDto> result = cryptoRecommendationService.getSortedByNormalizedRangedForAllCryptos()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        logger.info("Successfully retrieved normalized range for each crypto in descending order.");
        return result;
    }

    private CryptoNormalizedRangeDto toDto(CryptoNormalizedRange cryptoNormalizedRange){
        logger.debug("Converting CryptoNormalizedRange to CryptoNormalizedRangeDto for symbol: {}", cryptoNormalizedRange.getSymbol());
        CryptoNormalizedRangeDto cryptoNormalizedRangeDto = new CryptoNormalizedRangeDto();
        cryptoNormalizedRangeDto
                .normalizedRange(cryptoNormalizedRange.getNormalizedRange())
                .symbol(cryptoNormalizedRange.getSymbol());
        logger.debug("Successfully converted CryptoNormalizedRange to CryptoNormalizedRangeDto for symbol: {}", cryptoNormalizedRange.getSymbol());
        return cryptoNormalizedRangeDto;
    }
}