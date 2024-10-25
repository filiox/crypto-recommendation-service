package com.xm.crypto.recommendation.controller.command;

import com.xm.crypto.recommendation.dto.CryptoNormalizedRangeDto;
import com.xm.crypto.recommendation.model.CryptoNormalizedRange;
import com.xm.crypto.recommendation.model.Request;
import com.xm.crypto.recommendation.service.CryptoRecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class GetSortedByNormalizedRangeCommand implements Command<Request, List<CryptoNormalizedRangeDto>> {

    private static final Logger logger = LoggerFactory.getLogger(GetSortedByNormalizedRangeCommand.class);

    private final CryptoRecommendationService cryptoRecommendationService;

    public GetSortedByNormalizedRangeCommand(CryptoRecommendationService cryptoRecommendationService) {
        this.cryptoRecommendationService = requireNonNull(cryptoRecommendationService, "cryptoRecommendationService is required");
    }

    @Override
    public List<CryptoNormalizedRangeDto> execute(Request requestDto) {
        logger.info("Getting normalized range for each crypto in descending order.");
        return cryptoRecommendationService.getSortedByNormalizedRange()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CryptoNormalizedRangeDto toDto(CryptoNormalizedRange cryptoNormalizedRange){
        logger.debug("Converting CryptoNormalizedRange to CryptoNormalizedRangeDto for symbol: {}", cryptoNormalizedRange.getSymbol());
        CryptoNormalizedRangeDto cryptoNormalizedRangeDto = new CryptoNormalizedRangeDto();
        return cryptoNormalizedRangeDto
                .normalizedRange(cryptoNormalizedRange.getNormalizedRange())
                .symbol(cryptoNormalizedRange.getSymbol());
    }
}